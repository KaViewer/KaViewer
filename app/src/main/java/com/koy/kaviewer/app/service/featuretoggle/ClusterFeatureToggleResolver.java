package com.koy.kaviewer.app.service.featuretoggle;

import com.koy.kaviewer.common.configuration.KaViewerConfiguration;
import com.koy.kaviewer.common.constant.CommonConstant;
import com.koy.kaviewer.common.entity.PermissionVO;
import com.koy.kaviewer.common.toggle.FeatureToggle;
import com.koy.kaviewer.common.toggle.Operations;
import com.koy.kaviewer.common.toggle.Toggle;
import com.koy.kaviewer.common.toggle.toggles.ClusterToggles;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ClusterFeatureToggleResolver extends FeatureToggleConditionResolver {

    public ClusterFeatureToggleResolver(KaViewerConfiguration kaViewerConfiguration) {
        super(kaViewerConfiguration);
    }

    @Override
    public boolean enable(FeatureToggle featureToggle) {
        return enable(ClusterToggles.CREATE, featureToggle.operation());
    }

    @Override
    public Class<? extends Toggle<?>> supportToggleGroup() {
        return ClusterToggles.class;
    }


    @Override
    Map<String, Boolean> getConfigTogglesFromConfiguration(KaViewerConfiguration config) {
        return config.filter(CommonConstant.CLUSTER);
    }

    @Override
    public List<PermissionVO> permission() {
        final ClusterToggles[] clusterToggles = ClusterToggles.values();
        final Map<Operations, Boolean> toggles = new HashMap<>(clusterToggles.length);
        final PermissionVO permissionVO = new PermissionVO();
        permissionVO.setType(ClusterToggles.class);
        permissionVO.setToggles(toggles);

        Arrays.stream(clusterToggles).forEach(it -> {
            final boolean enable = enable(it, it.getOperation());
            toggles.put(it.getOperation(), enable);
        });
        return List.of(permissionVO);
    }


}
