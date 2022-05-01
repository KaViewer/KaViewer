package com.koy.kaviewer.shell.core;

import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.ParameterDescription;
import org.springframework.shell.ParameterResolver;
import org.springframework.shell.Utils;
import org.springframework.shell.ValueResult;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.metadata.MethodDescriptor;
import javax.validation.metadata.ParameterDescriptor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.shell.Utils.unCamelify;

@Component
@Order(0)
public class KaViewerParameterResolver implements ParameterResolver {
    private final ConversionService conversionService;

    public KaViewerParameterResolver(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired(required = false)
    public void setValidatorFactory(ValidatorFactory validatorFactory) {
        this.validator = validatorFactory.getValidator();
    }


    // it is opposite of StandParameterResolver
    @Override
    public boolean supports(MethodParameter parameter) {
        boolean optOut = parameter.hasParameterAnnotation(ShellOption.class)
                && parameter.getParameterAnnotation(ShellOption.class).optOut();
        return optOut && parameter.getMethodAnnotation(ShellMethod.class) != null;
    }

    /**
     * the wordsBuffer status on different cases:
     * topic describe                       null
     * topic describe -t                   [-t]
     * topic describe -t t1 t2 t3          [-t,t1,t2,t3]
     */
    @Override
    public ValueResult resolve(MethodParameter methodParameter, List<String> wordsBuffer) {
        String prefix = prefixForMethod(Objects.requireNonNull(methodParameter.getMethod()));
        List<String> words = wordsBuffer.stream().filter(w -> !w.isEmpty()).collect(Collectors.toList());
        // remove the prefix to get all the params
        words.remove(prefix);
        KaViewerParameterResolver.ParameterRawValue parameterRawValue =
                new KaViewerParameterResolver.ParameterRawValue(Joiner.on(",").join(words), true, null, 0, 0);
        Object value = convertRawValue(parameterRawValue, methodParameter);
        return new ValueResult(methodParameter, value);
    }


    private Object convertRawValue(KaViewerParameterResolver.ParameterRawValue parameterRawValue, MethodParameter methodParameter) {
        String s = parameterRawValue.value;
        if (ShellOption.NULL.equals(s)) {
            return null;
        } else {
            return conversionService.convert(s, TypeDescriptor.valueOf(String.class),
                    new TypeDescriptor(methodParameter));
        }
    }

    private String prefixForMethod(Executable method) {
        return method.getAnnotation(ShellMethod.class).prefix();
    }

    private Optional<String> defaultValueFor(Parameter parameter) {
        ShellOption option = parameter.getAnnotation(ShellOption.class);
        if (option != null && !ShellOption.NONE.equals(option.defaultValue())) {
            return Optional.of(option.defaultValue());
        } else if (getArity(parameter) == 0) {
            return Optional.of("false");
        }
        return Optional.empty();
    }

    @Override
    public Stream<ParameterDescription> describe(MethodParameter parameter) {
        Parameter jlrParameter = parameter.getMethod().getParameters()[parameter.getParameterIndex()];
        int arity = getArity(jlrParameter);
        Class<?> type = parameter.getParameterType();
        ShellOption option = jlrParameter.getAnnotation(ShellOption.class);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arity; i++) {
            if (i > 0) {
                sb.append(" ");
            }
            sb.append(arity > 1 ? unCamelify(removeMultiplicityFromType(parameter).getSimpleName())
                    : unCamelify(type.getSimpleName()));
        }
        ParameterDescription result = ParameterDescription.outOf(parameter);
        result.formal(sb.toString());
        if (option != null) {
            result.help(option.help());
            Optional<String> defaultValue = defaultValueFor(jlrParameter);
            if (defaultValue.isPresent()) {
                result.defaultValue(defaultValue.map(dv -> dv.equals(ShellOption.NULL) ? "<none>" : dv).get());
            }
        }
        result
                .keys(getKeysForParameter(parameter.getMethod(), parameter.getParameterIndex())
                        .collect(Collectors.toList()))
                .mandatoryKey(false);

        MethodDescriptor constraintsForMethod = validator.getConstraintsForClass(parameter.getDeclaringClass())
                .getConstraintsForMethod(parameter.getMethod().getName(), parameter.getMethod().getParameterTypes());
        if (constraintsForMethod != null) {
            ParameterDescriptor constraintsDescriptor = constraintsForMethod
                    .getParameterDescriptors().get(parameter.getParameterIndex());
            result.elementDescriptor(constraintsDescriptor);
        }

        return Stream.of(result);
    }

    @Override
    public List<CompletionProposal> complete(MethodParameter methodParameter, CompletionContext context) {
        return null;
    }

    /**
     * In case of {@code foo[] or Collection<Foo>} and arity > 1, return the element type.
     */
    private Class<?> removeMultiplicityFromType(MethodParameter parameter) {
        Class<?> parameterType = parameter.getParameterType();
        if (parameterType.isArray()) {
            return parameterType.getComponentType();
        } else if (Collection.class.isAssignableFrom(parameterType)) {
            return parameter.getNestedParameterType();
        } else {
            throw new RuntimeException("For " + parameter + " (with arity > 1) expected an array/collection type");
        }
    }

    /**
     * Return the arity of a given parameter. The default arity is 1, except for booleans
     * where arity is 0 (can be overridden back to 1 via an annotation)
     */
    private int getArity(Parameter parameter) {
        ShellOption option = parameter.getAnnotation(ShellOption.class);
        int inferred = (parameter.getType() == boolean.class || parameter.getType() == Boolean.class) ? 0 : 1;
        return option != null && option.arity() != ShellOption.ARITY_USE_HEURISTICS ? option.arity() : inferred;
    }

    /**
     * Return the key(s) for the i-th parameter of the command method, resolved either from
     * the {@link ShellOption} annotation, or from the actual parameter name.
     */
    private Stream<String> getKeysForParameter(Method method, int index) {
        Parameter p = method.getParameters()[index];
        return getKeysForParameter(p);
    }

    private Stream<String> getKeysForParameter(Parameter p) {
        Executable method = p.getDeclaringExecutable();
        String prefix = prefixForMethod(method);
        ShellOption option = p.getAnnotation(ShellOption.class);
        if (option != null && option.value().length > 0) {
            return Arrays.stream(option.value());
        } else {
            return Stream.of(prefix + Utils.unCamelify(Objects.requireNonNull(Utils.createMethodParameter(p).getParameterName())));
        }
    }

    private static class ParameterRawValue {


        private Integer from;

        private Integer to;

        /**
         * The raw String value that got bound to a parameter.
         */
        private final String value;

        /**
         * If false, the value resolved is the result of applying defaults.
         */
        private final boolean explicit;

        /**
         * The key that was used to set the parameter, or null if resolution happened by position.
         */
        private final String key;

        private ParameterRawValue(String value, boolean explicit, String key, Integer from, Integer to) {
            this.value = value;
            this.explicit = explicit;
            this.key = key;
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return "ParameterRawValue{" +
                    "value='" + value + '\'' +
                    ", explicit=" + explicit +
                    ", key='" + key + '\'' +
                    ", from=" + from +
                    ", to=" + to +
                    '}';
        }
    }

}
