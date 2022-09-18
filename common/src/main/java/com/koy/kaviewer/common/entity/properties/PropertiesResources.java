package com.koy.kaviewer.common.entity.properties;

import lombok.Data;

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

        private Predicate<String> from;

        public Predicate<String> getFrom() {
            return from;
        }

        ResourcesType() {
            this.from = (any) -> false;
        }

        ResourcesType(Predicate<String> from) {
            this.from = from;
        }

        public static final EnumSet<ResourcesType> types = EnumSet.allOf(ResourcesType.class);

        public static ResourcesType from(String source) {
            return types.stream().filter(it -> it.from.test(source)).findFirst().orElseGet(() -> null);
        }
    }
}
