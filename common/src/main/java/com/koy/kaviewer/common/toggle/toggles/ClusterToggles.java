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
    READ_CLUSTER_META(2, Operations.READ),
    ;

    private final int offset;
    private final Operations operation;

    @Override
    public ClusterToggles[] toggles() {
        return values();
    }

}
