package com.koy.kaviewer.web.api;

import com.koy.kaviewer.common.entity.TopicMetaVO;
import com.koy.kaviewer.common.entity.TopicVO;
import com.koy.kaviewer.common.service.TopicService;
import com.koy.kaviewer.web.KaViewerWebApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/topic")
@CrossOrigin(origins = "*")
public class TopicController {

    @GetMapping
    public ResponseEntity<Set<String>> list(@RequestHeader(name = "k-cluster") String cluster) {
        final TopicService topicService = KaViewerWebApplication.getBean(TopicService.class);
        final Set<String> topics = topicService.list(cluster);
        return ResponseEntity.ok(topics);

    }

    @PostMapping
    public ResponseEntity<Void> createTopic(@RequestBody TopicVO newTopic) {
        final TopicService topicService = KaViewerWebApplication.getBean(TopicService.class);
        topicService.create(newTopic);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/meta")
    public ResponseEntity<List<TopicMetaVO>> listMeta(@RequestHeader(name = "k-cluster") String cluster) {
        final TopicService topicService = KaViewerWebApplication.getBean(TopicService.class);
        final List<TopicMetaVO> topicMetaVOS = topicService.listMeta(cluster);
        return ResponseEntity.ok(topicMetaVOS);

    }

    @DeleteMapping
    public ResponseEntity<List<TopicMetaVO>> delete(@RequestParam(value = "topic", required = true) String topic) {
        final TopicService topicService = KaViewerWebApplication.getBean(TopicService.class);
        topicService.delete(topic);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
