package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.entity.PermissionVO;
import com.koy.kaviewer.common.toggle.KaViewerMode;
import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.ToggleResolver;
import com.koy.kaviewer.web.KaViewerWebApplication;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public abstract class FeatureToggleConditionResolver implements ToggleResolver {

//    @Lazy
//    @Autowired
//    KaViewerConfiguration kaViewerConfiguration;

    public <T extends Toggle<T>> boolean enable(T toggle, Operations operations) {
        final int toggleMask = toggle.toggleMaskFromOperation(operations);
        return (toggleMask & mergeModeMask(toggle, modeMask(toggle))) != 0;
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

        AtomicInteger mask = new AtomicInteger(modeMask);
        configToggles.forEach((key, toEnable) ->
                Arrays.stream(toggle.toggles()).filter(el -> el.getOperation().name().equalsIgnoreCase(key))
                        .findFirst()
                        .ifPresent(it -> {
                            final int toggleMask = it.toggleMask();
                            int tmp = mask.get();
                            if (toEnable) {
                                mask.set(tmp | toggleMask);
                            } else {
                                mask.set(tmp & ~toggleMask);
                            }
                        }));

        return mask.get();
    }

    protected <T extends Toggle<T>> List<PermissionVO> doPermission(Class<T> clz, T toggle) {
        final T[] targetToggles = toggle.toggles();
        final Map<Operations, Boolean> toggles = new HashMap<>(targetToggles.length);
        final PermissionVO permissionVO = new PermissionVO();
        permissionVO.setType(clz);
        permissionVO.setToggles(toggles);

        Arrays.stream(targetToggles).forEach(it -> {
            final boolean enable = enable(it, it.getOperation());
            toggles.put(it.getOperation(), enable);
        });
        return List.of(permissionVO);
    }


}
