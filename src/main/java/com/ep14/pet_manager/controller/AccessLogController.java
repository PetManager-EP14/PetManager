package com.ep14.pet_manager.controller;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ep14.pet_manager.entity.AccessLog;
import com.ep14.pet_manager.repository.AccessLogRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/access-log")
@RequiredArgsConstructor
public class AccessLogController {

    private final AccessLogRepository repo;

    @GetMapping
    @PreAuthorize("hasAuthority('report.read')")
    public List<AccessLog> search(
        @RequestParam(required = false) UUID userId,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant from,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant to) {

        return repo.search(userId, from, to);
    }
}