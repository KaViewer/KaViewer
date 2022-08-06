package com.koy.kaviewer.app.backup;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PersistKafkaProperties implements ApplicationListener<ContextClosedEvent> {
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        // ignore child kafka close event
        // KaViewerApplication.getRoot()
        if (Objects.nonNull(event.getApplicationContext().getParent())) {
            return;
        }

        // TODO backup all the properties for rollout
    }
}