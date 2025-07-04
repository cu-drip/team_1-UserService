package com.example.userservice.security;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserHeaderAuthFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String userId = request.getHeader("X-User-UUID");
        String userHash = request.getHeader("X-User-Hash");
        String path = request.getRequestURI();
        System.out.println("UserHeaderAuthFilter: METHOD: " + request.getMethod() + ", PATH: " + path);
        boolean isPublic = path.startsWith("/api/auth/");

        if (!isPublic) {
            if (userId == null || userHash == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"message\":\"Missing auth headers\"}");
                return;
            }
            try {
                UUID uuid = UUID.fromString(userId);
                Optional<User> userOpt = userRepository.findById(uuid);
                if (userOpt.isEmpty()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"message\":\"User not found\"}");
                    return;
                }
                User user = userOpt.get();
                if (!user.getHashedPassword().equals(userHash)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"message\":\"Invalid user hash\"}");
                    return;
                }
                // Пример RBAC: только isAdmin может PATCH любого пользователя
                if (request.getMethod().equals("PATCH") && path.matches("/users/[^/]+")) {
                    if (!user.isAdmin()) {
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write("{\"message\":\"Forbidden: admin only\"}");
                        return;
                    }
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"message\":\"Invalid user id\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
} 