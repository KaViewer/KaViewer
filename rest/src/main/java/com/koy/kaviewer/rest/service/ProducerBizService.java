package com.koy.kaviewer.rest.service;

import com.koy.kaviewer.kafka.ipc.ProducerService;
import com.koy.kaviewer.rest.KaViewerRestApplication;
import com.koy.kaviewer.rest.domain.MessageVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class ProducerBizService {

    public void publish(MessageVO messageVO) {
        publish(messageVO.getTopic(),
                messageVO.getPartition(),
                messageVO.getHeaders(),
                messageVO.getKey().getBytes(StandardCharsets.UTF_8),
                messageVO.getValue().getBytes(StandardCharsets.UTF_8));
    }

    public void publish(MultipartFile key, MultipartFile val, String topic, int partition, Map<String, Object> headers) {
        if (key.isEmpty() || val.isEmpty()) {
            return;
        }

        try {
            final byte[] keyContent = key.getBytes();
            final byte[] valContent = val.getBytes();
            publish(topic, partition, headers, keyContent, valContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, int partition, Map<String, Object> headers, byte[] key, byte[] val) {

        final ProducerService producer = KaViewerRestApplication.getBean(ProducerService.class);
        producer.publish(topic, partition, headers, key, val);
    }

}
