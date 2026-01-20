package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * A {@link jakarta.servlet.Filter} for Servlet web applications that adds trace ID to the response
 * headers.
 *
 * @see TraceProvider
 */
public class ProblemTracingWebFilter extends OncePerRequestFilter {

  private final TraceProvider traceProvider;

  /**
   * Constructs a new {@code ProblemTracingWebFilter} with the provided {@link TraceProvider}.
   *
   * @param traceProvider the {@link TraceProvider} to use for obtaining trace information, may be
   *     {@code null}
   */
  ProblemTracingWebFilter(@Nullable TraceProvider traceProvider) {
    this.traceProvider = traceProvider;
  }

  /** {@inheritDoc} */
  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    ImmutablePair<@NotEmpty String, String> trace = this.traceProvider.getTraceId();
    response.setHeader(trace.getKey(), trace.getValue());
    filterChain.doFilter(request, response);
  }
}
