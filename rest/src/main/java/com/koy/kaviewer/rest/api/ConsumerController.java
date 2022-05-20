package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.rest.domain.MessageRecord;
import com.koy.kaviewer.rest.service.ConsumerBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topic")
public class ConsumerController {

    @Autowired
    ConsumerBizService consumerBizService;

    @GetMapping("/string/{topic}/p/{partition}/s/{sorted}/{limit}")
    public ResponseEntity<List<MessageRecord<String, String>>> fetch(
            @PathVariable(name = "topic") String topic,
            @PathVariable(name = "partition") Integer partition,
            @PathVariable(name = "sorted") String sorted,
            @PathVariable(name = "limit") Integer limit) {
        final List<MessageRecord<String, String>> records = consumerBizService.fetchInString(topic, partition, limit, sorted);
        return ResponseEntity.ok(records);

    }

    @GetMapping("/byte/{topic}/p/{partition}/s/{sorted}/{limit}")
    public ResponseEntity<List<MessageRecord<byte[], byte[]>>> fetchByte(
            @PathVariable(name = "topic") String topic,
            @PathVariable(name = "partition") Integer partition,
            @PathVariable(name = "sorted") String sorted,
            @PathVariable(name = "limit") Integer limit) {
        final List<MessageRecord<byte[], byte[]>> records = consumerBizService.fetchInByte(topic, partition, limit, sorted);
        return ResponseEntity.ok(records);

    }

}
