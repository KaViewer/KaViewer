package com.koy.kaviewer.app.service.endpoint;

import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.web.KaViewerWebApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.beans.BeansEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.web.annotation.WebEndpoint;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@WebEndpoint(id = "contexts")
public class KaViewerBeansEndpoint {
    private final ConfigurableApplicationContext kaViewerApplicationContext;

    @Autowired
    public KaViewerBeansEndpoint(ConfigurableApplicationContext kaViewerApplicationContext) {
        this.kaViewerApplicationContext = kaViewerApplicationContext;
    }

    @ReadOperation
    public BeansEndpoint.ApplicationBeans beans() {
        final ConfigurableApplicationContext rest = (ConfigurableApplicationContext) KaViewerWebApplication.getRest();
        final BeansEndpoint restBeanEndPoint = new BeansEndpoint(rest);
        final BeansEndpoint.ApplicationBeans restBeans = restBeanEndPoint.beans();
        final ConcurrentLinkedQueue<ApplicationContext> kafkaApplicationContexts = KafkaApplicationHolder.getKafkaApplicationContexts();
        kafkaApplicationContexts.forEach(kafkaApplicationContext -> {
            final BeansEndpoint kafkaBeanEndPoint = new BeansEndpoint((ConfigurableApplicationContext) kafkaApplicationContext);
            final BeansEndpoint.ApplicationBeans kafkaBeans = kafkaBeanEndPoint.beans();
            restBeans.getContexts().put(kafkaApplicationContext.getId(), kafkaBeans.getContexts().get(kafkaApplicationContext.getId()));
        });
        return restBeans;
    }
}
