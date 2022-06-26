package com.koy.kaviewer.rest.api;

import com.koy.kaviewer.rest.domain.BrokerVO;
import com.koy.kaviewer.rest.service.BrokerBizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/broker")
@Tag(name = "Broker Operations", description = "Do operations on broker.")
public class BrokerController {

    private final BrokerBizService brokerBizService;

    @Autowired
    public BrokerController(BrokerBizService brokerBizService) {
        this.brokerBizService = brokerBizService;
    }

    @Operation(summary = "Get all brokers' info of a cluster.")
    @GetMapping(value = {"/", "/{cluster}"})
    public ResponseEntity<List<BrokerVO>> list(@PathVariable(name = "cluster", required = false) String cluster) {
        final List<BrokerVO> brokers = brokerBizService.brokers(cluster);
        return ResponseEntity.ok(brokers);

    }
}
