package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.rest.domain.dto.BrokerVO;
import com.koy.kaviewer.rest.service.BrokerBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/broker")
public class BrokerController {

    @Autowired
    BrokerBizService brokerBizService;

    @GetMapping
    public ResponseEntity<List<BrokerVO>> list(@RequestHeader(name = "k-cluster") String cluster) {
        final List<BrokerVO> brokers = brokerBizService.brokers(cluster);
        return ResponseEntity.ok(brokers);

    }
}
