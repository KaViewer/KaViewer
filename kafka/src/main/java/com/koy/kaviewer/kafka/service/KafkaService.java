package com.koy.kaviewer.kafka.service;

import com.koy.kaviewer.kafka.config.ConfigResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {
    @Autowired
    ConfigResolver configResolver;

}
