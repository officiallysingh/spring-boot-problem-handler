package com.ksoot.problem.spring.advice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * An {@link AccessDeniedHandler} that delegates exceptions to Spring WebMVC's {@link
 * HandlerExceptionResolver} as defined in {@link WebMvcConfigurationSupport}.
 */
public class ProblemAccessDeniedHandler implements AccessDeniedHandler {

  private final HandlerExceptionResolver resolver;

  /**
   * Constructs a new problem access denied handler with the given resolver.
   *
   * @param resolver the handler exception resolver
   */
  public ProblemAccessDeniedHandler(final HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  /** {@inheritDoc} */
  @Override
  public void handle(
      final @NonNull HttpServletRequest request,
      final @NonNull HttpServletResponse response,
      final @NonNull AccessDeniedException exception) {
    this.resolver.resolveException(request, response, null, exception);
  }
}
