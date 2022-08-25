package com.koy.kaviewer.common.toggle.toggles;

import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ConsumerToggles implements Toggle<ConsumerToggles> {
    READ(0, Operations.READ),
    WRITE(1, Operations.WRITE),
    ;

    private final int flag;
    private final Operations operations;

    @Override
    public int lite() {
        return READ.offset();
    }

    @Override
    public ConsumerToggles[] toggles() {
        return values();
    }
}
