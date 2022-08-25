package com.koy.kaviewer.common.toggle.toggles;

import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TopicToggles implements Toggle<TopicToggles> {
    CREAT(0, Operations.CREATE),
    DELETE(1, Operations.DELETE),
    ;

    private final int flag;
    private final Operations operations;

    @Override
    public int lite() {
        return 0;
    }

    @Override
    public TopicToggles[] toggles() {
        return values();
    }


}
