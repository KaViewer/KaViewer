package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.constant.CommonConstant;
import com.koy.kaviewer.common.entity.PermissionVO;
import com.koy.kaviewer.common.toggle.FeatureToggle;

import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.ConsumerToggles;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConsumerFeatureToggleResolver extends FeatureToggleConditionResolver {

    public ConsumerFeatureToggleResolver(KaViewerConfiguration kaViewerConfiguration) {
        super(kaViewerConfiguration);
    }

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        final Operations operation = featureToggle.operation();
        return enable(ConsumerToggles.READ_MESSAGE, operation);
    }

    @Override
    public Class<? extends Toggle<?>> supportToggleGroup() {
        return ConsumerToggles.class;
    }


    @Override
    Map<String, Boolean> getConfigTogglesFromConfiguration(KaViewerConfiguration config) {
        return config.filter(CommonConstant.CONSUMER);
    }

    @Override
    public List<PermissionVO> permission() {
        final ConsumerToggles[] consumerToggles = ConsumerToggles.values();
        final Map<Operations, Boolean> toggles = new HashMap<>(consumerToggles.length);
        final PermissionVO permissionVO = new PermissionVO();
        permissionVO.setType(ConsumerToggles.class);
        permissionVO.setToggles(toggles);

        Arrays.stream(consumerToggles).forEach(it -> {
            final boolean enable = enable(it, it.getOperation());
            toggles.put(it.getOperation(), enable);
        });
        return List.of(permissionVO);
    }


}
