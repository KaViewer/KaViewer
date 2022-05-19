package com.koy.kaviewer.rest.service;

import com.koy.kaviewer.kafka.ipc.ConsumerService;
import com.koy.kaviewer.rest.KaViewerRestApplication;
import com.koy.kaviewer.rest.domain.MessageRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConsumerBizService {

    public List<MessageRecord<String, String>> fetch(String topic, int partition, int size, String sorted) {
        // check
        final ConsumerService consumerService = KaViewerRestApplication.getBean(ConsumerService.class);
        final List<ConsumerRecord<String, String>> records = consumerService.fetchMessage(topic, partition, size, sorted);
        return records.stream().map(rds ->
                new MessageRecord<String, String>(
                        rds.topic(),
                        rds.partition(),
                        rds.offset(),
                        rds.timestamp(),
                        rds.timestampType(),
                        rds.serializedKeySize(),
                        rds.serializedValueSize(),
                        rds.headers(),
                        rds.key(),
                        rds.value()
                )).collect(Collectors.toList());
    }
}
