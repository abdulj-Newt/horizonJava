package com.example.customerservice.config;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String userId = request.getHeader("x-user-id");
            System.out.println("[UserIdFilter] User ID from header: " + userId);
            String threadName = Thread.currentThread().getName();
            System.out.println("Running in thread from header: " + threadName);
            if (userId != null && !userId.trim().isEmpty()) {
                UserContextHolder.setUserId(userId);
                MDC.put("userId", userId); // Optional: for logging
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContextHolder.clear();
            MDC.remove("userId");
        }
    }
}
