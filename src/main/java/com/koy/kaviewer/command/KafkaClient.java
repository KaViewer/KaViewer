package com.koy.kaviewer.command;

import com.koy.kaviewer.command.client.AdminClientCfg;
import com.koy.kaviewer.command.client.KafkaAdminClientWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.concurrent.TimeUnit;

@ShellComponent
public class KafkaClient {

    @Autowired
    private ApplicationContext applicationContext;

    @ShellMethod(value = "login to bootstrap.sever, -h host -p port", prefix = "-", key = "login")
    public void login(@ShellOption(defaultValue = "localhost") String h, @ShellOption(defaultValue = "9092") String p) {
        KafkaAdminClientWrapper kafkaAdminClient = applicationContext.getAutowireCapableBeanFactory().getBean(KafkaAdminClientWrapper.class);
        AdminClientCfg cfg = new AdminClientCfg();
        cfg.setBrokerAddress(h, p);
        cfg.setTimeOut(60L, TimeUnit.SECONDS);
        kafkaAdminClient.config(cfg);
        kafkaAdminClient.create();
    }
}
