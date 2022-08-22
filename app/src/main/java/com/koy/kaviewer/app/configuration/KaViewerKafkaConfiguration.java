package com.koy.kaviewer.app.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class KaViewerKafkaConfiguration {
    String cluster;
    String kafkaClusterVersion;
    String zookeeperServer;
    String bootstrapService;
    String jaasConfig;
}
