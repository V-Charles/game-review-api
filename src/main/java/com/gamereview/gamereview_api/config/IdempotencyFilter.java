package com.gamereview.gamereview_api.config;

import com.gamereview.gamereview_api.service.IdempotencyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    @Autowired
    private IdempotencyService idempotencyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && !request.getRequestURI().contains("swagger") && !request.getRequestURI().contains("api-docs")) {

            String idempotencyKey = request.getHeader("X-Idempotency-Key");

            if (idempotencyKey == null || idempotencyKey.isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"erro\": \"Cabecalho X-Idempotency-Key e obrigatorio para operacoes POST.\"}");
                return;
            }

            if (idempotencyService.isProcessed(idempotencyKey)) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("{\"erro\": \"Requisicao duplicada. A operacao com esta chave ja foi processada.\"}");
                return;
            }

            idempotencyService.markAsProcessed(idempotencyKey);
        }

        filterChain.doFilter(request, response);
    }
}