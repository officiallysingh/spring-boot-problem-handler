package com.ksoot.problem.spring.advice.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
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
public class ProblemAccessDeniedHandler implements AccessDeniedHandler {

  private final HandlerExceptionResolver resolver;

  public ProblemAccessDeniedHandler(
      @Qualifier("handlerExceptionResolver") final HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  @Override
  public void handle(
      final HttpServletRequest request,
      final HttpServletResponse response,
      AccessDeniedException exception)
      throws IOException, ServletException {
    this.resolver.resolveException(request, response, null, exception);
  }
}
