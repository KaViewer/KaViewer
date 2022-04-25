package com.koy.kaviewer.common;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

public class DefaultValues {

    public static final String DEFAULT_ALL = "__ALL";
    public static final String DEFAULT_NONE = "__NONE";
    private static final Set<String> values = new LinkedHashSet<>();

    static {
        Field[] fields = DefaultValues.class.getFields();
        Arrays.stream(fields).forEach(it -> {
            boolean publicStaticFinal = ReflectionUtils.isPublicStaticFinal(it);
            if (publicStaticFinal) {
                try {
                    String val = (String) it.get(null);
                    values.add(val);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public static boolean isDefaultValue(String val) {
        return values.contains(val);
    }

    public static boolean isNotDefaultValue(String val){
        return !isDefaultValue(val);
    }
}
