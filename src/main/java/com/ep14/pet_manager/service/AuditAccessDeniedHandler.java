package com.ep14.pet_manager.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.ep14.pet_manager.entity.AccessLog;
import com.ep14.pet_manager.repository.AccessLogRepository;
import com.ep14.pet_manager.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditAccessDeniedHandler implements AccessDeniedHandler {

    private final AccessLogRepository repo;
    private final UserRepository userRepository;

    @Override
    public void handle(HttpServletRequest req,
                       HttpServletResponse res,
                       AccessDeniedException ex) throws IOException {

        UUID userId = null;
        String username = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            username = auth.getName(); // con JWT, normalmente es el email
            if (username != null && !username.isBlank()) {
                var uOpt = userRepository.findByEmail(username);
                if (uOpt.isPresent()) {
                    userId = uOpt.get().getUser_id();
                }
            }
        }

        String required = (String) req.getAttribute("requiredPermission"); // puede ser null

        AccessLog log = new AccessLog();
        log.setUserId(userId);
        log.setPath(req.getRequestURI());
        log.setMethod(req.getMethod());
        log.setRequiredPermission(required);
        log.setDecision("DENIED");
        log.setReason((username != null ? ("user=" + username + " :: ") : "") + ex.getMessage());
        log.setRemoteAddr(req.getRemoteAddr());
        repo.save(log);

        res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        res.setContentType("application/json");
        res.getWriter().write("{\"code\":403,\"message\":\"Insufficient permission\"}");
    }
}
