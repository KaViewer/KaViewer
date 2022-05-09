package com.koy.kaviewer.app.core.event;


import com.koy.kaviewer.app.core.KaViewerProcessor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class ClientProcessor  implements KaViewerProcessor<RestEvent, RestEvent> {
    @Override
    public void onSubscribe(Subscription s) {

    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void subscribe(Subscriber<? super RestEvent> s) {

    }
}
