package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.constant.CommonConstant;
import com.koy.kaviewer.common.entity.PermissionVO;
import com.koy.kaviewer.common.toggle.FeatureToggle;

import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.ConsumerToggles;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class ConsumerFeatureToggleResolver extends FeatureToggleConditionResolver {

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
        return doPermission(ConsumerToggles.class, ConsumerToggles.READ_MESSAGE);
    }


}
