package com.gamereview.gamereview_api.config;

import com.gamereview.gamereview_api.exception.FilterErrorResponseWriter;
import com.gamereview.gamereview_api.service.IdempotencyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    @Autowired
    private IdempotencyService idempotencyService;

    @Autowired
    private FilterErrorResponseWriter errorResponseWriter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if ("POST".equalsIgnoreCase(request.getMethod()) && !request.getRequestURI().contains("swagger") && !request.getRequestURI().contains("api-docs")) {

            String idempotencyKey = request.getHeader("X-Idempotency-Key");

            if (idempotencyKey == null || idempotencyKey.isBlank()) {
                errorResponseWriter.writeError(
                        request,
                        response,
                        HttpStatus.BAD_REQUEST,
                        "Cabeçalho Obrigatório Ausente",
                        "O cabeçalho X-Idempotency-Key é obrigatório para operações POST."
                );
                return;
            }

            if (idempotencyService.isProcessed(idempotencyKey)) {
                errorResponseWriter.writeError(
                        request,
                        response,
                        HttpStatus.CONFLICT,
                        "Conflito de Idempotência",
                        "Requisição duplicada. A operação com esta chave de idempotência já foi processada."
                );
                return;
            }

            idempotencyService.markAsProcessed(idempotencyKey);
        }

        filterChain.doFilter(request, response);
    }
}