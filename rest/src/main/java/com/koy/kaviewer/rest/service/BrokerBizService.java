package com.koy.kaviewer.rest.service;

import com.koy.kaviewer.kafka.ipc.BrokerService;
import com.koy.kaviewer.rest.KaViewerRestApplication;
import com.koy.kaviewer.rest.domain.dto.BrokerVO;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class BrokerBizService {

    public List<BrokerVO> brokers(String cluster) {
        final BrokerService brokerService = KaViewerRestApplication.getBean(BrokerService.class);
        final DescribeClusterResult brokers = brokerService.describeClusters(cluster);
        List<BrokerVO> brokerVOs = List.of();
        try {
            brokerVOs = brokers.nodes().get().stream().map(node -> new BrokerVO(node.id(), node.host(), node.port(), node.rack()))
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return brokerVOs;
    }
}
