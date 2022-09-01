package com.koy.kaviewer.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class BrokerVO {
    private final Integer id;
    private final String host;
    private final Integer port;
    private final String rack;
}

