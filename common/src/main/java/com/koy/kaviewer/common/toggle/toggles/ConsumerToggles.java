package com.koy.kaviewer.common.toggle.toggles;

import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConsumerToggles implements Toggle<ConsumerToggles> {
    READ_MESSAGE(0, Operations.READ),
    PUBLISH_MESSAGE(1, Operations.WRITE),
    ;

    private final int flag;
    private final Operations operation;

    @Override
    public int lite() {
        return READ_MESSAGE.offset();
    }

    @Override
    public ConsumerToggles[] toggles() {
        return values();
    }
}
