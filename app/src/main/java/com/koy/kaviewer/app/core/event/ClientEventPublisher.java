package com.koy.kaviewer.app.core.event;

import com.koy.kaviewer.app.core.KaViewerPublisher;
import org.reactivestreams.Subscriber;

public class ClientEventPublisher implements KaViewerPublisher<RestEvent> {

    @Override
    public void subscribe(Subscriber<? super RestEvent> s) {

    }
}
