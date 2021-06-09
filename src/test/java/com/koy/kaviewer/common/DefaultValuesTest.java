package com.koy.kaviewer.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultValuesTest {
    @Test
    void testIsDefaultValues(){
        boolean exist = DefaultValues.isDefaultValue("__ALL__");
        Assertions.assertTrue(exist);

    }
}
