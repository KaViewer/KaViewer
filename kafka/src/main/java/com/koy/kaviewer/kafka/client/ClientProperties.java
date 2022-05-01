package com.koy.kaviewer.kafka.client;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.logging.log4j.util.Strings;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class ClientProperties extends Properties {
    private String host;
    private String port;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setBrokerAddress(String host, String port) {
        assert Strings.isNotBlank(host);
        assert Strings.isNotBlank(port);
        this.address = host + ":" + port;
        setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.address);
    }

    public void setTimeOut(Long duration, TimeUnit unit) {
        setProperty(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, String.valueOf(unit.toMillis(duration)));
    }

    @Override
    public String toString() {
        return "AdminClientCfg{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
