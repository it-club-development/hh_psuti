package com.example.demo.auth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWT_util jwtUtil;

    @Autowired
    private IpUtil ipUtil;  // ← ДЛЯ ЛОГИРОВАНИЯ IP

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String email = jwtUtil.extractEmail(token);
                String ip = ipUtil.getClientIp(request);
                System.out.println("✅ Аутентифицирован: " + email + " (IP: " + ip + ")");
                request.setAttribute("email", email);
                request.setAttribute("ip", ip);
            } else {
                System.out.println("❌ Невалидный токен от: " + ipUtil.getClientIp(request));
            }
        }

        chain.doFilter(request, response);
    }
}