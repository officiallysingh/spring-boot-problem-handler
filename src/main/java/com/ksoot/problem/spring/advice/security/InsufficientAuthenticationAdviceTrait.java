package com.ksoot.problem.spring.advice.security;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface InsufficientAuthenticationAdviceTrait<T, R> extends AdviceTrait<T, R> {

	@ExceptionHandler
	default R handleInsufficientAuthenticationException(
			final InsufficientAuthenticationException exception, final T request) {
		Problem problem = toProblem(exception, HttpStatus.UNAUTHORIZED,
				ProblemMessageSourceResolver.of(
						ProblemConstant.DETAIL_CODE_PREFIX
								+ GeneralErrorKey.SECURITY_UNAUTHORIZED,
						exception.getMessage()));
		return create(exception, request, HttpStatus.UNAUTHORIZED, problem);
	}
}
