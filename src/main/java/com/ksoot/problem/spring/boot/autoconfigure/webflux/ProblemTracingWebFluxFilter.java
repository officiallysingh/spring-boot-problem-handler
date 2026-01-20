package com.ksoot.problem.spring.boot.autoconfigure.webflux;

import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * A {@link org.springframework.web.server.WebFilter} for WebFlux applications that adds trace ID to
 * the response headers.
 *
 * @see TraceProvider
 */
public class ProblemTracingWebFluxFilter implements WebFilter {

  private final TraceProvider traceProvider;

  /**
   * Constructs a new {@code ProblemTracingWebFluxFilter} with the provided {@link TraceProvider}.
   *
   * @param traceProvider the {@link TraceProvider} to use for obtaining trace information, may be
   *     {@code null}
   */
  public ProblemTracingWebFluxFilter(@Nullable TraceProvider traceProvider) {
    this.traceProvider = traceProvider;
  }

  /** {@inheritDoc} */
  @Override
  public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    if(Objects.nonNull(this.traceProvider)) {
      ImmutablePair<@NotEmpty String, String> trace = this.traceProvider.getTraceId();
      exchange.getResponse().getHeaders().add(trace.getKey(), trace.getValue());
    }
    return chain.filter(exchange);
  }
}
