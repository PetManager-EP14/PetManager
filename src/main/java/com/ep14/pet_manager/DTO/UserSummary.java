package com.ep14.pet_manager.DTO;

import java.util.Set;

public record UserSummary(
    String userId,
    String name,
    String email,
    String roleCode,
    Set<String> effectivePermissions
) {}