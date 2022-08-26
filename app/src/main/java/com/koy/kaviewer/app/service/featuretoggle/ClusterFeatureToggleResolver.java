package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.constant.CommonConstant;
import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.ClusterToggles;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ClusterFeatureToggleResolver extends FeatureToggleConditionResolver {

    public ClusterFeatureToggleResolver(KaViewerConfiguration kaViewerConfiguration) {
        super(kaViewerConfiguration);
    }

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        return enable(ClusterToggles.CREAT, featureToggle.operation());
    }

    @Override
    public Class<? extends Toggle<?>> supportToggleGroup() {
        return ClusterToggles.class;
    }


    @Override
    Map<String, Boolean> getConfigTogglesFromConfiguration(KaViewerConfiguration config) {
        return config.filter(CommonConstant.CLUSTER);
    }

}
