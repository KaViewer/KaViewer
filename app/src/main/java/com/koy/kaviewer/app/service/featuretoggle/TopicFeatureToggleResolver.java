package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.constant.CommonConstant;
import com.koy.kaviewer.common.entity.PermissionVO;
import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.TopicToggles;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TopicFeatureToggleResolver extends FeatureToggleConditionResolver {

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        return enable(TopicToggles.CREAT_TOPIC, featureToggle.operation());
    }

    @Override
    public List<PermissionVO> permission() {
        return doPermission(TopicToggles.class, TopicToggles.CREAT_TOPIC);
    }


    @Override
    public Class<? extends Toggle<?>> supportToggleGroup() {
        return TopicToggles.class;
    }

    @Override
    Map<String, Boolean> getConfigTogglesFromConfiguration(KaViewerConfiguration config) {
        return config.filter(CommonConstant.TOPIC);
    }
}
