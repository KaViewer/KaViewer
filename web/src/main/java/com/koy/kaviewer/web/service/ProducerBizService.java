package com.koy.kaviewer.web.service;

import com.koy.kaviewer.common.exception.KaViewerBizException;
import com.koy.kaviewer.common.service.ProducerService;
import com.koy.kaviewer.web.KaViewerWebApplication;
import com.koy.kaviewer.web.domain.HeaderVO;
import com.koy.kaviewer.web.domain.MessageVO;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProducerBizService {

    public void publish(MessageVO messageVO) {
        publish(messageVO.getTopic(),
                messageVO.getPartition(),
                messageVO.getHeaders(),
                messageVO.getKey().getBytes(StandardCharsets.UTF_8),
                messageVO.getValue().getBytes(StandardCharsets.UTF_8));
    }

    public void publish(String key, MultipartFile val, String topic, int partition, List<HeaderVO> headers) {

        try {
            final byte[] keyContent = key.getBytes();
            final byte[] valContent = val.getBytes();
            publish(topic, partition, headers, keyContent, valContent);
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
    }

    public void publish(MultipartFile key, String val, String topic, int partition, List<HeaderVO> headers) {

        try {
            final byte[] keyContent = key.getBytes();
            final byte[] valContent = val.getBytes();
            publish(topic, partition, headers, keyContent, valContent);
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
    }


    public void publish(MultipartFile key, MultipartFile val, String topic, int partition, List<HeaderVO> headers) {

        try {
            final byte[] keyContent = key.getBytes();
            final byte[] valContent = val.getBytes();
            publish(topic, partition, headers, keyContent, valContent);
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
    }

    public void publish(String topic, int partition, List<HeaderVO> headers, byte[] key, byte[] val) {

        final Map<String, Object> headersMap = headers.stream().collect(Collectors.toMap(HeaderVO::getKey, HeaderVO::getValue));
        final ProducerService producer = KaViewerWebApplication.getBean(ProducerService.class);
        producer.publish(topic, partition, headersMap, key, val);
    }

}
