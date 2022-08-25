package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.ClusterToggles;
import com.koy.kaviewer.common.toggle.toggles.ConsumerToggles;
import org.springframework.stereotype.Component;

@Component
public class ClusterFeatureToggleResolver extends FeatureToggleConditionResolver {

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        return enable(ClusterToggles.CREAT, featureToggle.operation());
    }

    @Override
    public Class<? extends Toggle<?>> supportToggleGroup() {
        return ClusterToggles.class;
    }

}
