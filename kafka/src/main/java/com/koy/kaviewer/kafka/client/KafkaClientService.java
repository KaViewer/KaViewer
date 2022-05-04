package com.koy.kaviewer.kafka.client;

import com.koy.kaviewer.kafka.config.ConfigResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class KafkaClientService {
    @Autowired
    ConfigResolver configResolver;


}
