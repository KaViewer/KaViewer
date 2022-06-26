package com.koy.kaviewer.rest.api.ctx;

import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.rest.service.ClusterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Cluster Operations", description = "Do operations on cluster.")
@RestController
@RequestMapping("/api/v1/cluster")
public class ClusterController {

    private final ClusterService clusterService;

    @Autowired
    public ClusterController(ClusterService clusterService) {
        this.clusterService = clusterService;
    }

    @Operation(summary = "Get all clusters' name.")
    @GetMapping
    public ResponseEntity<List<String>> list() {
        final List<String> clusters = clusterService.list();
        return ResponseEntity.ok(clusters);
    }

    @Operation(summary = "Create a new cluster.")
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody KafkaPropertiesVO kafkaPropertiesVO) {
        clusterService.create(kafkaPropertiesVO);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Operation(summary = "Create a new cluster.")
    @PostMapping("/a")
    public ResponseEntity<Void> create(@RequestParam("attachment") MultipartFile attachment) {
        clusterService.create(attachment);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @Operation(summary = "Delete a cluster.")
    @DeleteMapping("/{cluster}")
    public ResponseEntity<Void> delete(@PathVariable("cluster") String cluster) {
        clusterService.delete(cluster);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}
