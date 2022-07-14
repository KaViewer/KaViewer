package com.koy.kaviewer.kafka.application;

import com.koy.kaviewer.kafka.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.kafka.exception.ErrorMsg;
import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"com.koy.kaviewer.kafka"})
public class KafkaApplication implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaApplication.class);
    private static final ConcurrentLinkedQueue<ApplicationContext> kafkaApplicationContexts = new ConcurrentLinkedQueue<>();
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
        LOGGER.info("Remove cluster: {}", clusterName);
        final KafkaApplicationCacheEntity kafkaApplicationCacheEntity = clusterHolder.remove(clusterName);
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
        if (!contains(key)) {
            throw KaViewerBizException.of(ErrorMsg.NO_CLUSTER_FOUND);
        }
        return getKafkaApplication(key).getKafkaApplicationContext().getBean(clz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        kafkaApplicationContexts.add(applicationContext);
    }

    public static ConcurrentLinkedQueue<ApplicationContext> getKafkaApplicationContexts() {
        return kafkaApplicationContexts;
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        final ApplicationContext applicationContext = event.getApplicationContext();
        LOGGER.info("ApplicationContext closed: {}", applicationContext.getId());
        kafkaApplicationContexts.remove(applicationContext);
    }
}
