package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.kafka.application.KafkaApplication;
import com.koy.kaviewer.kafka.entity.TopicMetaVO;
import com.koy.kaviewer.kafka.ipc.TopicService;
import com.koy.kaviewer.rest.KaViewerRestApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/topic")
public class TopicController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicController.class);

    @GetMapping
    public ResponseEntity<Set<String>> list(@RequestHeader(name = "k-cluster") String cluster) {
        final TopicService topicService = KaViewerRestApplication.getBean(TopicService.class);
        final Set<String> topics = topicService.list(cluster);
        return ResponseEntity.ok(topics);

    }

    @GetMapping("/meta")
    public ResponseEntity<List<TopicMetaVO>> listMeta(@RequestHeader(name = "k-cluster") String cluster) {
        final TopicService topicService = KaViewerRestApplication.getBean(TopicService.class);
        final List<TopicMetaVO> topicMetaVOS = topicService.listMeta(cluster);
        return ResponseEntity.ok(topicMetaVOS);

    }
}
