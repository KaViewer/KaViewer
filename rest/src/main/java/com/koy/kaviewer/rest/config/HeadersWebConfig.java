package com.koy.kaviewer.rest.config;

import com.koy.kaviewer.rest.domain.HeaderVO;

import com.koy.kaviewer.rest.domain.MessageHeaders;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class HeadersWebConfig implements WebMvcConfigurer, HandlerMethodArgumentResolver {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(this);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Objects.nonNull(parameter.getParameterAnnotation(MessageHeaders.class));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        final String parameterKeyName = parameter.getParameter().getName();
        final StandardMultipartHttpServletRequest request = (StandardMultipartHttpServletRequest) webRequest.getNativeRequest();
        // headers[0].xxx
        // headers[0].val
        // headers[0] = key
        // headers[0] = val
        List<HeaderVO> headers = new ArrayList<>();
        request.getParameterMap()
                .entrySet()
                .stream()
                .filter(entry -> entry.getKey().startsWith(parameterKeyName + "["))
                .map(entry -> Pair.of(entry.getKey().split("\\.")[0], entry.getValue()[0]))
                .collect(Collectors.groupingBy(it -> it.getKey().split("\\.")[0]))
                .values()
                .forEach(it -> {
                    final Pair<String, String> keyPair = it.get(0);
                    final Pair<String, String> valPair = it.get(1);
                    Object val = valPair.getValue();
                    try {
                        val = Long.parseLong(String.valueOf(val));
                    } catch (Exception ignore) {

                    }
                    headers.add(new HeaderVO(keyPair.getValue(), val));
                });
        return headers;
    }
}
