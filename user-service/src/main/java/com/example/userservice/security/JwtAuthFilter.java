package com.example.userservice.security;

import com.example.userservice.model.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtProvider;
    private final UserRepository userRepo;

    public JwtAuthFilter(JwtTokenProvider jwtProvider, UserRepository userRepo) {
        this.jwtProvider = jwtProvider;
        this.userRepo    = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {
        String path = req.getRequestURI();
        boolean isPublic = path.startsWith("/api/v1/auth/");

        if (!isPublic) {
            String header = req.getHeader("Authorization");
            if (header == null || !header.toLowerCase().startsWith("bearer ")) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("{\"message\":\"401: Unauthorized\"}");
                return;
            }

            String token = header.substring(header.indexOf(' ') + 1);
            if (!jwtProvider.validateToken(token)) {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("{\"message\":\"401: Unauthorized\"}");
                return;
            }

            UUID userId = jwtProvider.getUserIdFromToken(token);
            User user = userRepo.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<SimpleGrantedAuthority> authorities = jwtProvider.getRolesFromToken(token).stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        chain.doFilter(req, res);
    }
} 