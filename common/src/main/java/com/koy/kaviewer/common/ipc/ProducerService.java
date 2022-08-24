package com.koy.kaviewer.common.ipc;

import java.util.Map;

public interface ProducerService {
    boolean publish(String topic, int partition, Map<String, Object> headers, byte[] key, byte[] val);
}
