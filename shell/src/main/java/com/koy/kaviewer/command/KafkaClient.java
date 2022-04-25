package com.koy.kaviewer.command;

import com.koy.kaviewer.command.client.AdminClientCfg;
import com.koy.kaviewer.command.client.KafkaAdminClientWrapper;
import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@ShellComponent
public class KafkaClient {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    @Lazy
    private Terminal terminal;

    @ShellMethod(value = "login to bootstrap.sever, -h host -p port", prefix = "-", key = "login")
    public void login(@ShellOption(defaultValue = "localhost") String h, @ShellOption(defaultValue = "9092") String p) {
        KafkaAdminClientWrapper kafkaAdminClient = applicationContext.getAutowireCapableBeanFactory().getBean(KafkaAdminClientWrapper.class);
        // in case of calling this duplicated
        if (Objects.nonNull(kafkaAdminClient.getDelegate())) {
            String address = kafkaAdminClient.getConfig().getAddress();
            terminal.writer().println(
                    new AttributedStringBuilder()
                            .append("You already login in ", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                            .append("Host:" + address, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED).bold())
                            .append(", go ahead!", AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
                            .toAnsi()
            );
            terminal.writer().flush();
            return;
        }
        AdminClientCfg cfg = new AdminClientCfg();
        cfg.setBrokerAddress(h, p);
        cfg.setTimeOut(60L, TimeUnit.SECONDS);
        kafkaAdminClient.config(cfg);
        kafkaAdminClient.create();
    }
}
