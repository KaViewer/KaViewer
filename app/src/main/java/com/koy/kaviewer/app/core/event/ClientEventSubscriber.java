package com.koy.kaviewer.app.core.event;

import com.koy.kaviewer.app.core.KaViewerSubscriber;
import org.reactivestreams.Subscription;

public class ClientEventSubscriber implements KaViewerSubscriber<RestEvent> {
    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
