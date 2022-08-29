package com.koy.kaviewer.common.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import lombok.Data;

import java.util.Locale;
import java.util.Map;

@Data
public class PermissionVO {

    private Class<? extends Toggle<?>> type;
    private Map<Operations, Boolean> toggles = Map.of();

    @JsonGetter("type")
    public String type() {
        return type.getSimpleName().replace("Toggles","").toLowerCase(Locale.ROOT);
    }
}
