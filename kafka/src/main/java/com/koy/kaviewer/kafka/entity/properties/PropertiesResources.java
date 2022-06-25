package com.koy.kaviewer.kafka.entity.properties;

import java.util.EnumSet;
import java.util.function.Predicate;

public class PropertiesResources<T> {

    private T resource;
    private ResourcesType type;

    public T getResource() {
        return resource;
    }

    public void setResource(T resource) {
        this.resource = resource;
    }

    public ResourcesType getType() {
        return type;
    }

    public void setType(ResourcesType type) {
        this.type = type;
    }

    public enum ResourcesType {
        YAML(s -> s.endsWith(".yaml") || s.endsWith(".yml")),
        ENTITY(s -> s.equalsIgnoreCase("entity")),
        CONFIGMAP(s -> s.equalsIgnoreCase("configMap")),
        CRD;

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
