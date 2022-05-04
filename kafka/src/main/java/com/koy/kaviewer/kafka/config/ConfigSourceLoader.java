package com.koy.kaviewer.kafka.config;

import java.io.FileNotFoundException;

public interface ConfigSourceLoader<T, S> {

    T load(S source) throws FileNotFoundException;

    boolean support(String fileName);

}
