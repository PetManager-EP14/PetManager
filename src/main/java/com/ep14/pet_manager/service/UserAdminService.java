package com.ep14.pet_manager.service;

import java.util.Set;

import com.ep14.pet_manager.DTO.UserSummary;

public interface UserAdminService {
    UserSummary getUserSummary(String userId);
    void assignRole(String userId, Long roleId);
    void assignDirectPermissions(String userId, Set<String> codes);
}
