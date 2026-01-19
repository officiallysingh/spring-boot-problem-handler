package com.ksoot.problem.spring.advice.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * A compound {@link AuthenticationEntryPoint} and {@link AccessDeniedHandler} that delegates
 * exceptions to Spring WebMVC's {@link HandlerExceptionResolver} as defined in {@link
 * WebMvcConfigurationSupport}.
 *
 * <p>Compatible with spring-webmvc 4.3.3.
 */
public class ProblemAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final HandlerExceptionResolver resolver;

  public ProblemAuthenticationEntryPoint(final HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void commence(
      final @NonNull HttpServletRequest request,
      final @NonNull HttpServletResponse response,
      final @NonNull AuthenticationException exception) {
    this.resolver.resolveException(request, response, null, exception);
  }
}
