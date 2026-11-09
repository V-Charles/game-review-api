package com.gamereview.gamereview_api.config;

import com.gamereview.gamereview_api.model.ApiKey;
import com.gamereview.gamereview_api.repository.ApiKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ApiKeyAuthFilter extends OncePerRequestFilter {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String apiKeyHeader = request.getHeader("X-API-Key");

        if (apiKeyHeader != null) {
            boolean isValida = apiKeyRepository.findByChave(apiKeyHeader)
                    .map(ApiKey::isAtivo)
                    .orElse(false);

            if (isValida) {
                ApiKeyAuthentication auth = new ApiKeyAuthentication(apiKeyHeader, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"erro\": \"API Key invalida ou inativa.\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}