package com.koy.kaviewer.common.toggle.toggles;

import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ClusterToggles implements Toggle<ClusterToggles> {
    CREATE_CLUSTER(0, Operations.CREATE),
    DELETE_CLUSTER(1, Operations.DELETE),
    ;

    private final int flag;
    private final Operations operation;

    @Override
    public int lite() {
        return 0;
    }

    @Override
    public ClusterToggles[] toggles() {
        return values();
    }

}
