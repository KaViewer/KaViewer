package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.rest.domain.BrokerVO;
import com.koy.kaviewer.rest.service.BrokerBizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/broker")
public class BrokerController {

    private final BrokerBizService brokerBizService;

    @Autowired
    public BrokerController(BrokerBizService brokerBizService) {
        this.brokerBizService = brokerBizService;
    }

    @GetMapping
    public ResponseEntity<List<BrokerVO>> list() {
        final List<BrokerVO> brokers = brokerBizService.brokers();
        return ResponseEntity.ok(brokers);

    }
}
