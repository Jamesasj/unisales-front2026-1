package com.unisales.piemanager.bootstrap.controller;

import com.unisales.piemanager.bootstrap.dto.BootstrapResponse;
import com.unisales.piemanager.bootstrap.service.BootstrapService;
import com.unisales.piemanager.common.api.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class BootstrapController {

    private final BootstrapService bootstrapService;

    public BootstrapController(BootstrapService bootstrapService) {
        this.bootstrapService = bootstrapService;
    }

    @PostMapping("/bootstrap")
    public ApiResponse<BootstrapResponse> bootstrap() {
        BootstrapResponse result = bootstrapService.initialize();
        String message = result.isAdminCreated() ? "Initial data created" : "Initial data already exists";
        return ApiResponse.success(message, result);
    }
}
