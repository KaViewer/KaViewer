package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.rest.domain.MessageRecord;
import com.koy.kaviewer.rest.service.ConsumerBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topic")
public class ConsumerController {

    @Autowired
    ConsumerBizService consumerBizService;

    @GetMapping("/{topic}/p/{partition}")
    public ResponseEntity<List<MessageRecord<String, String>>> fetch(
            @PathVariable(name = "topic") String topic,
            @PathVariable(name = "partition") Integer partition,
            @RequestParam(name = "sorted", required = false, defaultValue = "desc") String sorted,
            @RequestParam(name = "limit", required = false, defaultValue = "100") Integer limit,
            @RequestParam(name = "keyDeserializer", required = false, defaultValue = "string") String keyDeserializer,
            @RequestParam(name = "valDeserializer", required = false, defaultValue = "string") String valDeserializer) {

        if (partition < 0 || limit < 0) {
            return ResponseEntity.badRequest().build();
        }
        final List<MessageRecord<String, String>> records = consumerBizService.fetch(topic, partition, limit, sorted, keyDeserializer, valDeserializer);
        return ResponseEntity.ok(records);
    }
}
