package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.rest.domain.HeaderVO;
import com.koy.kaviewer.rest.domain.MessageHeaders;
import com.koy.kaviewer.rest.domain.MessageVO;
import com.koy.kaviewer.rest.service.ProducerBizService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/producer")
public class ProducerController {
    @Autowired
    ProducerBizService producerBizService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> publish(@RequestBody MessageVO messageVO) {
        if (messageVO.inValid()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        producerBizService.publish(messageVO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> publish(
            @RequestParam(name = "key") String key,
            @RequestParam(name = "value") MultipartFile val,
            @RequestParam(name = "topic") String topic,
            @RequestParam(name = "partition") int partition,
            @MessageHeaders(name = "headers") List<HeaderVO> headers

    ) {
        if (StringUtils.isEmpty(key) || val.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        producerBizService.publish(key, val, topic, partition, headers);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> publish(
            @RequestParam(name = "key") MultipartFile key,
            @RequestParam(name = "value") MultipartFile val,
            @RequestParam(name = "topic") String topic,
            @RequestParam(name = "partition") int partition,
            @MessageHeaders(name = "headers") List<HeaderVO> headers

    ) {
        if (key.isEmpty() || val.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        producerBizService.publish(key, val, topic, partition, headers);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
