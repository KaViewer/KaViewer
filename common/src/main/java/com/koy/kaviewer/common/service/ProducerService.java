package com.koy.kaviewer.common.service;

import java.util.Map;

public interface ProducerService {
    boolean publish(String topic, int partition, Map<String, Object> headers, byte[] key, byte[] val);
}
