package com.ksoot.problem.spring.boot.autoconfigure.webflux;

import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * A {@link org.springframework.web.server.WebFilter} for WebFlux applications that adds trace ID to
 * the response headers.
 *
 * @see TraceProvider
 */
@RequiredArgsConstructor
public class ProblemTracingWebFluxFilter implements WebFilter {

  private final TraceProvider traceProvider;

  /** {@inheritDoc} */
  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ImmutablePair<@NotEmpty String, String> trace = this.traceProvider.getTraceId();
    exchange.getResponse().getHeaders().add(trace.getKey(), trace.getValue());
    return chain.filter(exchange);
  }
}
