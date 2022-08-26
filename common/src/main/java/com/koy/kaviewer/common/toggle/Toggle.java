package com.koy.kaviewer.common.toggle;

import java.util.Arrays;

public interface Toggle<T extends Toggle<T>> {
    default int offset() {
        return (int) Math.pow(2, getFlag());
    }

    int getFlag();

    Operations getOperations();

    default int lite() {
        return 1;
    }

    default int full() {
        return Arrays.stream(toggles()).mapToInt(Toggle::offset).reduce((e1, e2) -> e1 | e2).getAsInt();
    }

    T[] toggles();

    default int offsetFrom(Operations operation) {
        return Arrays.stream(toggles()).filter(it -> it.getOperations() == operation).findFirst().map(Toggle::offset).orElseGet(() -> 0);
    }
}
