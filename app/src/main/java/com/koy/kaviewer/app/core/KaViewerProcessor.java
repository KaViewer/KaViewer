package com.koy.kaviewer.app.core;

import org.reactivestreams.Processor;

public interface KaViewerProcessor<T, R> extends Processor<T, R> {
    @Override
    default void onNext(T t){};
}
