package com.ksoot.problem.spring.boot.autoconfigure.webflux;

import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProblemTracingWebFluxFilter implements WebFilter {

  private final TraceProvider traceProvider;

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ImmutablePair<@NotEmpty String, String> trace = this.traceProvider.getTraceId();
    exchange.getResponse().getHeaders().add(trace.getKey(), trace.getValue());
    return chain.filter(exchange);
  }
}
