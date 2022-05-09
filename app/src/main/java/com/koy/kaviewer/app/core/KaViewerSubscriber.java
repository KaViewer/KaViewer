package com.koy.kaviewer.app.core;

import org.reactivestreams.Subscriber;

public interface KaViewerSubscriber<T> extends Subscriber<T> {

    @Override
    default void onNext(T t) {}
}
