package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.rest.domain.MessageVO;
import com.koy.kaviewer.rest.service.ProducerBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/producer")
public class ProducerController {
    @Autowired
    ProducerBizService producerBizService;

    @PostMapping
    public ResponseEntity<Void> publish(@RequestBody MessageVO messageVO) {
        producerBizService.publish(messageVO);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/attachment")
    public ResponseEntity<Void> publish(
            @RequestParam(name = "key") MultipartFile key,
            @RequestParam(name = "key") MultipartFile val,
            @RequestParam(name = "topic") String topic,
            @RequestParam(name = "partition") int partition,
            @RequestParam(name = "headers") Map<String, Object> headers
    ) {
        producerBizService.publish(key, val, topic, partition, headers);
        return ResponseEntity.ok(null);
    }
}
