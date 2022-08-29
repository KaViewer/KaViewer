package com.koy.kaviewer.common.toggle;


import com.koy.kaviewer.common.entity.PermissionVO;

import java.util.List;

public interface ToggleResolver {
    boolean enable(FeatureToggle featureToggle);

    List<PermissionVO> permission();


}
