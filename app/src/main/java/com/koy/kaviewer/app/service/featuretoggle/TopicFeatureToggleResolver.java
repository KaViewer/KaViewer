package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.TopicToggles;
import org.springframework.stereotype.Component;

@Component
public class TopicFeatureToggleResolver extends FeatureToggleConditionResolver {

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        return enable(TopicToggles.CREAT, featureToggle.operation());
    }


    @Override
    public Class<? extends Toggle<?>> supportToggleGroup() {
        return TopicToggles.class;
    }
}
