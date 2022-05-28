package com.koy.kaviewer.kafka.core;

import com.koy.kaviewer.kafka.ipc.ProducerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.springframework.core.convert.converter.Converter;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

public class KafkaProperties extends Properties {
    private String encoding = "UTF8";
    private String clusterName;
    private String kafkaClusterVersion;
    private String ZookeeperHost;
    private String zookeeperPort = "2181";
    private String bootstrapServers;
    private Security security;
    private String clientId;
    private ConsumerProperties consumer = new ConsumerProperties(this);
    private ProducerProperties producer = new ProducerProperties(this);

    public static class ConsumerProperties extends Properties {
        private static final AtomicInteger index = new AtomicInteger(0);
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
            this.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
            this.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
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

    public ProducerProperties getProducer() {
        return this.producer.buildProducerProperties();
    }

    public void setConsumer(ConsumerProperties consumer) {
        this.consumer = consumer;
    }

    public String getEncoding() {
        return encoding;
    }

    public static class ProducerProperties extends Properties {
        private static final AtomicInteger index = new AtomicInteger(0);
        private final String CLIENT_ID_PREFIX = "KaViewer::Consumer";
        private String clientId;
        public KafkaProperties kafkaProperties;

        public ProducerProperties(KafkaProperties kafkaProperties) {
            this.kafkaProperties = kafkaProperties;
        }

        public ProducerProperties buildProducerProperties() {
            this.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaProperties.getBootstrapServers());
            this.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
            this.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
            this.put(ProducerConfig.CLIENT_ID_CONFIG, getClientId("Client", index.getAndIncrement()));
            return this;

        }

        public String getClientId(String dot, int idx) {
            this.clientId = dot + "::" + CLIENT_ID_PREFIX + this.kafkaProperties.getClusterName() + "-" + idx;
            return clientId;
        }

    }

    interface KafkaPropertiesConverter<S> extends Converter<S, KafkaProperties> {


    }
}
