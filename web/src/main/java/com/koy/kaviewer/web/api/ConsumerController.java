package com.koy.kaviewer.web.api;

import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.toggles.ConsumerToggles;
import com.koy.kaviewer.web.domain.MessageRecord;
import com.koy.kaviewer.web.service.ConsumerBizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.Callable;

@Tag(name = "Consumer Operations", description = "Do operations on consumer.")
@RestController
@RequestMapping("/api/v1/topic")
@CrossOrigin(origins = "*")
public class ConsumerController {

    private final ConsumerBizService consumerBizService;

    @Autowired
    public ConsumerController(ConsumerBizService consumerBizService) {
        this.consumerBizService = consumerBizService;
    }

    @FeatureToggle(toggleGroup = ConsumerToggles.class, operation = Operations.READ)
    @Operation(summary = "Get range messages from a topic.")
    @GetMapping("/{topic}/p/{partition}")
    public ResponseEntity<List<MessageRecord<String, String>>> fetch(
            @PathVariable(name = "topic") String topic,
            @PathVariable(name = "partition") Integer partition,
            @RequestParam(name = "offset", required = false, defaultValue = "") String offset,
            @RequestParam(name = "limit", required = false, defaultValue = "100") Integer limit,
            @RequestParam(name = "keyDeserializer", required = false, defaultValue = "string") String keyDeserializer,
            @RequestParam(name = "valDeserializer", required = false, defaultValue = "string") String valDeserializer) {

        if (partition < -1 || limit < 0) {
            return ResponseEntity.badRequest().build();
        }

        final List<MessageRecord<String, String>> records = consumerBizService.fetch(topic, partition, limit, offset, keyDeserializer, valDeserializer);
        return ResponseEntity.ok(records);
    }
}
