package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.toggle.KaViewerMode;
import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.ToggleResolver;
import com.koy.kaviewer.web.KaViewerWebApplication;

public abstract class FeatureToggleConditionResolver implements ToggleResolver {

    public <T extends Toggle<T>> boolean enable(T toggle, Operations operations) {
        final int offset = toggle.offsetFrom(operations);
        return (offset & modeMask(toggle)) != 0;
    }

    public <T extends Toggle<T>> int modeMask(T toggle) {
        final KaViewerConfiguration kaViewerConfiguration = KaViewerWebApplication.getKaViewerConfiguration();
        final KaViewerMode mode = kaViewerConfiguration.getMode();
        if (KaViewerMode.LITE == mode) {
            return toggle.lite();
        }

        if (KaViewerMode.FULL == mode) {
            return toggle.full();
        }
        return 0;
    }

    abstract public Class<? extends Toggle<?>> supportToggleGroup();
}
