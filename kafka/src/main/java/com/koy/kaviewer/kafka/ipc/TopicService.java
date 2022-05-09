package com.koy.kaviewer.kafka.ipc;

import java.util.Set;

public interface TopicService {
    Set<String> list(String clusterName) ;
}
