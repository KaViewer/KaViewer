package com.koy.kaviewer.app.core;

import com.koy.kaviewer.app.core.event.Event;
import org.springframework.stereotype.Component;

import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class EventExecutor<T> {
    private static final ThreadPoolExecutor pool = new ThreadPoolExecutor(
            6,
            20,
            10,
            TimeUnit.MINUTES,
            new LinkedBlockingDeque<>(2000)
    );

    public Future<T> publish(Event<T> event) {
        return pool.submit(event);
    }

}
