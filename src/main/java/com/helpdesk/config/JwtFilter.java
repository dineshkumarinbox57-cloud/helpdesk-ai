package com.helpdesk.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("AUTH HEADER = " + authHeader);

        if (authHeader != null &&
            authHeader.startsWith("Bearer ")) {

            String token =
                    authHeader.substring(7);

                    System.out.println("TOKEN = " + token);

            try {
                String email =
                        JwtUtil.extractEmail(token);

                System.out.println(
                        "Authenticated User: "
                        + email);

            } catch (Exception e) {
                System.out.println("ERROR = " + e.getMessage());
                e.printStackTrace();
                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter()
                        .write("Invalid JWT Token");

                return;
            }
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}