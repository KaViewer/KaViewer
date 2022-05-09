package com.koy.kaviewer.app.core.event;

import java.util.concurrent.Callable;

public interface Event<V> extends Callable<V> {
    @Override
    default V call() throws Exception {
        return null;
    }

    enum EventType {
        CLIENT,
        CONSUMER,
        PRODUCER,
        ;

    }

    EventType type();

}
