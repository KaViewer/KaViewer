package com.koy.kaviewer.common.entity.properties;

import lombok.Data;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.function.Predicate;

@Data
public class PropertiesResources<T> {

    private T resource;
    private ResourcesType type;

    public enum ResourcesType {
        YAML(s -> s.endsWith(".yaml") || s.endsWith(".yml")),
        ENTITY(s -> s.equalsIgnoreCase("entity")),
        // dynamic import from kubernetes resources
//        CONFIGMAP(s -> s.equalsIgnoreCase("configMap")),
//        CRD
        ;

        private final Predicate<String> from;

        public Predicate<String> getFrom() {
            return from;
        }

        ResourcesType() {
            this.from = (any) -> false;
        }

        ResourcesType(Predicate<String> from) {
            this.from = from;
        }

        public static ResourcesType from(String source) {
            return Arrays.stream(values()).filter(it -> it.from.test(source)).findFirst().orElseGet(() -> null);
        }
    }
}
