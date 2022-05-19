package com.koy.kaviewer.kafka.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

public class KafkaProperties extends Properties {
    private String key;
    private String clusterName;
    private String kafkaClusterVersion;
    private String ZookeeperHost;
    private String zookeeperPort = "2181";
    private String bootstrapServers;
    private Security security;
    private String clientId;
    private ConsumerProperties consumer = new ConsumerProperties(this);

    public static class ConsumerProperties extends Properties {
        private static final AtomicInteger index = new AtomicInteger(0);
        public static final Function<String, Class<? extends Deserializer>> deserializerFrom = ds ->
                Stream.of(StringDeserializer.class, ByteArrayDeserializer.class)
                        .filter(clz -> clz.getSimpleName().toLowerCase(Locale.ROOT).startsWith(ds.toLowerCase(Locale.ROOT)))
                        .findFirst().orElseThrow(UnsupportedOperationException::new);
        private final String CLIENT_ID_PREFIX = "KaViewer::Consumer";
        // string / byte
        private String KeyDeserializer = "string";
        private String ValDeserializer = "string";
        private Integer maxPollRecords = 100;
        private String autoOffsetReset = "earliest";
        private String clientId;
        public KafkaProperties kafkaProperties;

        public ConsumerProperties(KafkaProperties kafkaProperties) {
            this.kafkaProperties = kafkaProperties;
        }

        public ConsumerProperties buildConsumerProperties() {
            final int idx = index.getAndIncrement();
            this.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaProperties.getBootstrapServers());
            this.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
            this.put(ConsumerConfig.GROUP_ID_CONFIG, getClientId("Group", idx));
            this.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deserializerFrom.apply(this.KeyDeserializer));
            this.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializerFrom.apply(this.ValDeserializer));
            this.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
            this.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, this.autoOffsetReset);
            this.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
            this.put(ConsumerConfig.CLIENT_ID_CONFIG, getClientId("Client", idx));
            return this;

        }

        public String getKeyDeserializer() {
            return KeyDeserializer;
        }

        public void setKeyDeserializer(String keyDeserializer) {
            KeyDeserializer = keyDeserializer;
        }

        public String getValDeserializer() {
            return ValDeserializer;
        }

        public void setValDeserializer(String valDeserializer) {
            ValDeserializer = valDeserializer;
        }

        public Integer getMaxPollRecords() {
            return maxPollRecords;
        }

        public void setMaxPollRecords(Integer maxPollRecords) {
            this.maxPollRecords = maxPollRecords;
        }

        public String getAutoOffsetReset() {
            return autoOffsetReset;
        }

        public void setAutoOffsetReset(String autoOffsetReset) {
            this.autoOffsetReset = autoOffsetReset;
        }

        public String getClientId(String dot, int idx) {
            this.clientId = dot + "::" + CLIENT_ID_PREFIX + this.kafkaProperties.getClusterName() + "-" + idx;
            return clientId;
        }
    }


    static class Security {
        private String type;
    }

    public KafkaProperties buildProperties() {
        this.clientId = "KaViewer::" + clusterName;
        setProperty(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.bootstrapServers);
        setProperty(AdminClientConfig.CLIENT_ID_CONFIG, this.clientId);
        return this;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getKafkaClusterVersion() {
        return kafkaClusterVersion;
    }

    public void setKafkaClusterVersion(String kafkaClusterVersion) {
        this.kafkaClusterVersion = kafkaClusterVersion;
    }

    public String getZookeeperHost() {
        return ZookeeperHost;
    }

    public void setZookeeperHost(String zookeeperHost) {
        ZookeeperHost = zookeeperHost;
    }

    public String getZookeeperPort() {
        return zookeeperPort;
    }

    public void setZookeeperPort(String zookeeperPort) {
        this.zookeeperPort = zookeeperPort;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        assert StringUtils.isNotEmpty(bootstrapServers);
        this.bootstrapServers = bootstrapServers;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getKey() {
        return clusterName + "-" + kafkaClusterVersion;
    }

    public ConsumerProperties getConsumer() {
        return this.consumer.buildConsumerProperties();
    }

    public void setConsumer(ConsumerProperties consumer) {
        this.consumer = consumer;
    }

    interface KafkaPropertiesConverter<S> extends Converter<S, KafkaProperties> {


    }
}
