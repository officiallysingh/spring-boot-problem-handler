package com.ksoot.problem.spring.advice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksoot.problem.spring.advice.webflux.SpringWebfluxProblemResponseUtils;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProblemServerAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {

  private final SecurityAdviceTraits<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> advice;

  private final ObjectMapper objectMapper;

  @Override
  public @NonNull Mono<Void> commence(
      final @NonNull ServerWebExchange exchange, final @NonNull AuthenticationException exception) {
    return this.advice
        .handleAuthenticationException(exception, exchange)
        .flatMap(
            entity ->
                SpringWebfluxProblemResponseUtils.writeResponse(
                    entity, exchange, this.objectMapper));
  }
}
