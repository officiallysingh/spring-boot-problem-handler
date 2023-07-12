package com.pchf.problem.spring.advice.security;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface InsufficientAuthenticationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleInsufficientAuthenticationException(final InsufficientAuthenticationException exception, final T request) {
    Problem problem = toProblem(exception, HttpStatus.UNAUTHORIZED,
        ProblemMessageSourceResolver.of(ProblemConstant.MESSAGE_CODE_PREFIX + GeneralErrorKey.SECURITY_UNAUTHORIZED,
            exception.getMessage()),
        ProblemMessageSourceResolver.of(ProblemConstant.DETAILS_CODE_PREFIX + GeneralErrorKey.SECURITY_UNAUTHORIZED,
            exception.getMessage()));
    return create(exception, request, HttpStatus.UNAUTHORIZED, problem);
  }
}
