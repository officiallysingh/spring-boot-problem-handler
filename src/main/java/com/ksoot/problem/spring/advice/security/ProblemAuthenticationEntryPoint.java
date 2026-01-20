package com.ksoot.problem.spring.advice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * An {@link AuthenticationEntryPoint} that delegates exceptions to Spring WebMVC's {@link
 * HandlerExceptionResolver} as defined in {@link WebMvcConfigurationSupport}.
 */
public class ProblemAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final HandlerExceptionResolver resolver;

  /**
   * Constructs a new problem authentication entry point with the given resolver.
   *
   * @param resolver the handler exception resolver
   */
  public ProblemAuthenticationEntryPoint(final HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  /** {@inheritDoc} */
  @Override
  public void commence(
      final @NonNull HttpServletRequest request,
      final @NonNull HttpServletResponse response,
      final @NonNull AuthenticationException exception) {
    this.resolver.resolveException(request, response, null, exception);
  }
}
