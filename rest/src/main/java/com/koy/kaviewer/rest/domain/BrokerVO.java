package com.koy.kaviewer.rest.domain;

public class BrokerVO {
    private final Integer id;
    private final String host;
    private final Integer port;
    private final String rack;

    public BrokerVO(Integer id, String host, Integer port, String rack) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.rack = rack;
    }

    public Integer getId() {
        return id;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getRack() {
        return rack;
    }
}

