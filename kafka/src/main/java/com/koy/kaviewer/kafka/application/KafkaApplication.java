package com.koy.kaviewer.kafka.application;

import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.kafka.share.RequestContextManagement;
import org.apache.kafka.common.requests.RequestContext;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.ConcurrentHashMap;

@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.kafka"})
public class KafkaApplication implements ApplicationContextAware {
    private static ApplicationContext parentKafka;
    private static final ConcurrentHashMap<String, KafkaApplicationCacheEntity> clusterHolder = new ConcurrentHashMap<>();

    public static void putIfAbsent(KafkaApplicationCacheEntity kafkaApplicationCacheEntity) {
        final String key = kafkaApplicationCacheEntity.getClusterName();
        if (clusterHolder.containsKey(key)) {
            return;
        }
        clusterHolder.put(key, kafkaApplicationCacheEntity);
    }

    public static KafkaApplicationCacheEntity getKafkaApplication(String key) {
        return clusterHolder.get(key);
    }

    public static <T> T getKafkaApplicationBean(Class<T> clz) {
        final String cluster = RequestContextManagement.getCluster();
        return getKafkaApplicationBean(cluster, clz);
    }

    public static <T> T getKafkaApplicationBean(String key, Class<T> clz) {
        return getKafkaApplication(key).getParentKafkaApplicationContext().getBean(clz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        KafkaApplication.parentKafka = applicationContext;
    }
}
