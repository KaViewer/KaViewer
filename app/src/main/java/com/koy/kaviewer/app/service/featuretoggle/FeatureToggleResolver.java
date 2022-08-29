package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.entity.PermissionVO;
import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.ToggleResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Primary
@RequiredArgsConstructor
public class FeatureToggleResolver implements ToggleResolver {

    private final List<FeatureToggleConditionResolver> featureToggleConditionResolver;

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        final Class<? extends Toggle<?>> toggleGroup = featureToggle.toggleGroup();
        return featureToggleConditionResolver.stream()
                .filter(it -> it.supportToggleGroup() == toggleGroup)
                .map(it -> it.enable(featureToggle))
                .findFirst()
                .orElseGet(() -> false);
    }

    @Override
    public List<PermissionVO> permission() {
        return featureToggleConditionResolver.stream().map(ToggleResolver::permission).flatMap(Collection::stream).collect(Collectors.toList());
    }


}
