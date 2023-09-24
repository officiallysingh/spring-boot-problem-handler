package com.ksoot.problem.spring.advice.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * A compound {@link AuthenticationEntryPoint} and {@link AccessDeniedHandler} that
 * delegates exceptions to Spring WebMVC's {@link HandlerExceptionResolver} as defined in
 * {@link WebMvcConfigurationSupport}.
 * <p>
 * Compatible with spring-webmvc 4.3.3.
 */
public class ProblemAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final HandlerExceptionResolver resolver;

	public ProblemAuthenticationEntryPoint(
			@Qualifier("handlerExceptionResolver") final HandlerExceptionResolver resolver) {
		this.resolver = resolver;
	}

	@Override
	public void commence(final HttpServletRequest request,
			final HttpServletResponse response, final AuthenticationException exception) {
		this.resolver.resolveException(request, response, null, exception);
	}
}
