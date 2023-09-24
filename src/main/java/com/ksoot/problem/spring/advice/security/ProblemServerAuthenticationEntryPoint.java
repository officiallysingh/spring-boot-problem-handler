package com.ksoot.problem.spring.advice.security;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksoot.problem.spring.advice.webflux.SpringWebfluxProblemResponseUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProblemServerAuthenticationEntryPoint
		implements ServerAuthenticationEntryPoint {

	private final SecurityAdviceTraits<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> advice;

	private final ObjectMapper objectMapper;

	@Override
	public Mono<Void> commence(final ServerWebExchange exchange,
			final AuthenticationException exception) {
		return this.advice.handleAuthenticationException(exception, exchange)
				.flatMap(entity -> SpringWebfluxProblemResponseUtils.writeResponse(entity,
						exchange, this.objectMapper));
	}
}
