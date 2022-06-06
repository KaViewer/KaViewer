package com.koy.kaviewer.app.ipc;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.share.RequestContextManagement;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProducerService implements com.koy.kaviewer.kafka.ipc.ProducerService {

    @Override
    public boolean publish(String topic, int partition, Map<String, Object> headers, byte[] key, byte[] val) {
        final String cluster = RequestContextManagement.getCluster();
        final com.koy.kaviewer.kafka.ipc.ProducerService producerService =
                KafkaApplication.getKafkaApplicationBean(cluster, com.koy.kaviewer.kafka.ipc.ProducerService.class);
        return producerService.publish(topic, partition, headers, key, val);
    }
}
