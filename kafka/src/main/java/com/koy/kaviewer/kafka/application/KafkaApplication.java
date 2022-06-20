package com.koy.kaviewer.kafka.application;

import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.kafka"})
public class KafkaApplication implements ApplicationContextAware {
    private static ApplicationContext parentKafka;
    private static final ConcurrentHashMap<String, KafkaApplicationCacheEntity> clusterHolder = new ConcurrentHashMap<>();

    public static boolean contains(String clusterName) {
        return clusterHolder.containsKey(clusterName);
    }

    public static List<String> listAll() {
        return clusterHolder.values().stream().map(KafkaApplicationCacheEntity::getClusterName).collect(Collectors.toList());
    }

    public static void remove(String clusterName) {
        if (!contains(clusterName)) {
            return;
        }

        final KafkaApplicationCacheEntity kafkaApplicationCacheEntity = clusterHolder.get(clusterName);
        final ConfigurableApplicationContext kafkaApplicationContext = (ConfigurableApplicationContext) kafkaApplicationCacheEntity.getKafkaApplicationContext();
        kafkaApplicationContext.close();
    }

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

    public static <T> T getKafkaApplicationBean(String key, Class<T> clz) {
        return getKafkaApplication(key).getKafkaApplicationContext().getBean(clz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        KafkaApplication.parentKafka = applicationContext;
    }
}
