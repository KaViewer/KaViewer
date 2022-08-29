package com.koy.kaviewer.web.api;

import com.koy.kaviewer.common.entity.PermissionVO;
import com.koy.kaviewer.web.core.FeatureToggleAdvice;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/permission")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PermissionController {
    private final FeatureToggleAdvice featureToggleAdvice;

    @GetMapping
    public ResponseEntity<List<PermissionVO>> permission() {
        final List<PermissionVO> permission = featureToggleAdvice.permission();
        return ResponseEntity.ok(permission);
    }
}
