package com.koy.kaviewer.kafka.application;

import com.koy.kaviewer.common.KafkaApplicationHolder;
import com.koy.kaviewer.common.entity.KafkaApplicationCacheEntity;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import java.util.List;

@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.kafka"})
@Slf4j
public class KafkaApplication implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

    public static boolean contains(String clusterName) {
        return KafkaApplicationHolder.contains(clusterName);
    }

    public static List<KafkaApplicationCacheEntity> listAll() {
        return KafkaApplicationHolder.listAll();
    }

    public static void putIfAbsent(KafkaApplicationCacheEntity kafkaApplicationCacheEntity) {
        final String key = kafkaApplicationCacheEntity.getClusterName();
        if (KafkaApplicationHolder.contains(key)) {
            return;
        }
        KafkaApplicationHolder.put(key, kafkaApplicationCacheEntity);
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        KafkaApplicationHolder.add(applicationContext);
    }


    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        final ApplicationContext applicationContext = event.getApplicationContext();
        log.info("ApplicationContext closed: {}", applicationContext.getId());
        KafkaApplicationHolder.remove(applicationContext);
    }
}
