package com.koy.kaviewer.kafka.core.remote;

import java.util.Set;

public interface TopicService {
    Set<String> list(String clusterName) ;
}
