package com.koy.kaviewer.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClusterVO {
    private String cluster;
    private List<BrokerVO> brokers;
    private Long createdTime;
}

