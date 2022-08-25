package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.toggle.FeatureToggle;

import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.ConsumerToggles;
import org.springframework.stereotype.Component;

@Component
public class ConsumerFeatureToggleResolver extends FeatureToggleConditionResolver {

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        final Operations operation = featureToggle.operation();
        return enable(ConsumerToggles.READ, operation);
    }

    @Override
    public Class<? extends Toggle<?>> supportToggleGroup() {
        return ConsumerToggles.class;
    }

}
