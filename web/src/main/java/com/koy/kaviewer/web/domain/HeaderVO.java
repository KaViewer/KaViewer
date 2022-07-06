package com.koy.kaviewer.web.domain;

public class HeaderVO {
    private String key;
    private Object value;

    public HeaderVO() {
    }

    public HeaderVO(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
