package com.koy.kaviewer.app.service;

import com.koy.kaviewer.app.configuration.KaViewerConfiguration;
import com.koy.kaviewer.app.configuration.KaViewerKafkaConfiguration;
import com.koy.kaviewer.app.service.resolver.KafkaPropertiesConvert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class KaViewerEnvScanner {
    private final KafkaApplicationSetupService kafkaApplicationSetupService;
    private final KaViewerConfiguration kaViewerConfiguration;
    private final KafkaPropertiesConvert kafkaPropertiesConvert;


    @PostConstruct
    public void scanEnv() {
        try {
            final KaViewerKafkaConfiguration kafkaConfiguration = kaViewerConfiguration.getKafka();
            kafkaApplicationSetupService.setUp(kafkaPropertiesConvert.convert(kafkaConfiguration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
