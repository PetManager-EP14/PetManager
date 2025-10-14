package com.ep14.pet_manager.dto;

import java.util.Set;

public record UserSummary(
    String userId,
    String name,
    String email,
    String roleCode,
    Set<String> effectivePermissions
) {}