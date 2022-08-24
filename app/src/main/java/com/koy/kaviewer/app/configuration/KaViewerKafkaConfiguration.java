package com.koy.kaviewer.app.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;


@Data
@NoArgsConstructor
public class KaViewerKafkaConfiguration {
    String cluster;
    String kafkaClusterVersion;
    String zookeeperServer;
    String bootstrapServers;
    String jaasConfig;

    public boolean invalid() {
        return StringUtils.isAnyEmpty(cluster, bootstrapServers);
    }
}
