package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.toggle.KaViewerMode;
import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.ToggleResolver;
import com.koy.kaviewer.web.KaViewerWebApplication;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public abstract class FeatureToggleConditionResolver implements ToggleResolver {

    public <T extends Toggle<T>> boolean enable(T toggle, Operations operations) {
        final int offset = toggle.offsetFrom(operations);
        return (offset & mergeModeMask(toggle, modeMask(toggle))) != 0;
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

    abstract Map<String, Boolean> getConfigTogglesFromConfiguration(KaViewerConfiguration config);

    private <T extends Toggle<T>> int mergeModeMask(T toggle, int modeMask) {
        final KaViewerConfiguration config = KaViewerWebApplication.getKaViewerConfiguration();
        final Map<String, Boolean> configToggles = getConfigTogglesFromConfiguration(config);

        AtomicInteger mask = new AtomicInteger(0);
        configToggles.forEach((key, toEnable) ->
                Arrays.stream(toggle.toggles()).filter(el -> el.getOperation().name().equalsIgnoreCase(key))
                        .findFirst()
                        .ifPresent(it -> {
                            final int offset = it.offset();
                            if (toEnable) {
                                var asIsEnable = (modeMask & offset) != 0;
                                if (!asIsEnable) {
                                    mask.addAndGet(+offset);
                                }
                            } else {
                                var asIsDisable = (modeMask & offset) == 0;
                                if (!asIsDisable) {
                                    mask.addAndGet(-offset);
                                }
                            }

                        }));

        return modeMask + mask.get();
    }

}
