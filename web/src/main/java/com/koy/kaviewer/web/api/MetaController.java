package com.koy.kaviewer.web.api;

import com.koy.kaviewer.kafka.entity.KafkaPropertiesVO;
import com.koy.kaviewer.web.service.MetaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/meta")
@CrossOrigin(origins = "*")
public class MetaController {
    private final MetaService metaService;

    public MetaController(MetaService metaService) {
        this.metaService = metaService;
    }

    @GetMapping
    public ResponseEntity<KafkaPropertiesVO> meta(){
        return ResponseEntity.ok(metaService.meta());
    }
}
