package com.koy.kaviewer.common.toggle;

import java.util.Arrays;

public interface Toggle<T extends Toggle<T>> {
    default int toggleMask() {
        return 1 << getOffset();
    }

    int getOffset();

    Operations getOperation();

    default int lite() {
        return 0;
    }

    default int full() {
        return Arrays.stream(toggles()).mapToInt(Toggle::toggleMask).reduce((e1, e2) -> e1 | e2).getAsInt();
    }

    T[] toggles();

    default int toggleMaskFromOperation(Operations operation) {
        return Arrays.stream(toggles()).filter(it -> it.getOperation() == operation).findFirst().map(Toggle::toggleMask).orElseGet(() -> 0);
    }
}
