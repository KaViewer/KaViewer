package com.koy.kaviewer.rest.api.ctx;

import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.rest.service.ClusterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/cluster")
public class ClusterController {


    @Autowired
    ClusterService clusterService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody KafkaPropertiesVO kafkaPropertiesVO) {
        clusterService.create(kafkaPropertiesVO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    // TODO
    @PostMapping("/a")
    public ResponseEntity<Void> create(@RequestParam("attachment") MultipartFile kafkaPropertiesVO) {
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
