package com.koy.kaviewer.common;

import com.koy.kaviewer.common.entity.KafkaApplicationCacheEntity;
import com.koy.kaviewer.common.entity.properties.KafkaProperties;
import com.koy.kaviewer.common.exception.ErrorMsg;
import com.koy.kaviewer.common.exception.KaViewerBizException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

public class KafkaApplicationHolder {
    private static final ConcurrentHashMap<String, KafkaApplicationCacheEntity> clusterHolder = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<ApplicationContext> kafkaApplicationContexts = new ConcurrentLinkedQueue<>();

    public static ConcurrentLinkedQueue<ApplicationContext> getKafkaApplicationContexts() {
        return kafkaApplicationContexts;
    }

    public static ConcurrentHashMap<String, KafkaApplicationCacheEntity> getClusterHolder() {
        return clusterHolder;
    }

    public static boolean contains(String clusterName) {
        return clusterHolder.containsKey(clusterName);
    }

    public static List<KafkaApplicationCacheEntity> listAll() {
        return clusterHolder.values().stream().map(KafkaApplicationCacheEntity::clone).collect(Collectors.toList());
    }

    public static void remove(String clusterName) {
        if (!contains(clusterName)) {
            return;
        }
        final KafkaApplicationCacheEntity kafkaApplicationCacheEntity = clusterHolder.remove(clusterName);
        final ConfigurableApplicationContext kafkaApplicationContext = (ConfigurableApplicationContext) kafkaApplicationCacheEntity.getKafkaApplicationContext();
        kafkaApplicationContext.close();
    }

    public static void add(ApplicationContext applicationContext) {
        kafkaApplicationContexts.add(applicationContext);
    }

    public static void remove(ApplicationContext applicationContext) {
        kafkaApplicationContexts.remove(applicationContext);
    }

    public static void put(String clusterName, KafkaApplicationCacheEntity kafkaApplicationCacheEntity) {
        clusterHolder.put(clusterName, kafkaApplicationCacheEntity);
    }

    public static KafkaApplicationCacheEntity get(String cluster) {
        return clusterHolder.get(cluster);
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

    public static List<KafkaProperties> getKafkaProperties() {
        return clusterHolder.values().stream().map(kafkaApplicationCacheEntity -> kafkaApplicationCacheEntity.getKafkaProperties().clone()).collect(Collectors.toList());
    }


}
