package com.pchf.problem.spring.advice.security;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Similar to 403 Forbidden, but specifically for use when authentication is
 * required and has failed or has not yet been provided.
 */
public interface AuthenticationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleAuthenticationException(final AuthenticationException exception, final T request) {
    Problem problem = toProblem(exception, HttpStatus.UNAUTHORIZED,
        ProblemMessageSourceResolver.of(ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.SECURITY_UNAUTHORIZED,
            exception.getMessage()));
    return create(exception, request, HttpStatus.UNAUTHORIZED, problem);
  }
}
