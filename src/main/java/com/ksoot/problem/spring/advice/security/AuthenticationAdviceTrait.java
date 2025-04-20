package com.ksoot.problem.spring.advice.security;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Similar to 403 Forbidden, but specifically for use when authentication is required and has failed
 * or has not yet been provided.
 */
public interface AuthenticationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleAuthenticationException(
      final AuthenticationException exception, final T request) {
    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(
                ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.SECURITY_UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.value()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.SECURITY_UNAUTHORIZED,
                HttpStatus.UNAUTHORIZED.getReasonPhrase()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.SECURITY_UNAUTHORIZED,
                exception.getMessage()));
    return toResponse(exception, request, HttpStatus.UNAUTHORIZED, problem);
  }
}
