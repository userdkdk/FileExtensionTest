package com.example.server.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//@Component
public class OriginCheckFilter extends OncePerRequestFilter {

    @Value("${app.domain.origins}")
    private List<String> allowedDomains;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String origin = request.getHeader("Origin");
        String referer = request.getHeader("Referer");

        boolean allowed = allowedDomains.stream().anyMatch(domain ->
                (origin != null && origin.startsWith(domain)) ||
                (referer != null && referer.startsWith(domain))
        );

        if (!allowed) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

