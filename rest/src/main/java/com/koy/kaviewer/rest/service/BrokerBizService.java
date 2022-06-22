package com.koy.kaviewer.rest.service;

import com.koy.kaviewer.kafka.exception.KaViewerBizException;
import com.koy.kaviewer.kafka.ipc.BrokerService;
import com.koy.kaviewer.rest.KaViewerRestApplication;
import com.koy.kaviewer.rest.domain.BrokerVO;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrokerBizService {

    public List<BrokerVO> brokers() {
        final BrokerService brokerService = KaViewerRestApplication.getBean(BrokerService.class);
        final DescribeClusterResult brokers = brokerService.describeClusters();
        List<BrokerVO> brokerVOs = List.of();
        try {
            brokerVOs = brokers.nodes().get().stream().map(node -> new BrokerVO(node.id(), node.host(), node.port(), node.rack()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw KaViewerBizException.of(e);
        }
        return brokerVOs;
    }
}
