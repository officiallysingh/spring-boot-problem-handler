package com.pchf.problem.spring.advice.security;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * The request was a valid request, but the server is refusing to respond to it.
 * The user might be logged in but does not have the necessary permissions for
 * the resource.
 */
public interface AccessDeniedAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleAccessDeniedException(final AccessDeniedException exception, final T request) {
    Problem problem = toProblem(exception, HttpStatus.FORBIDDEN,
        ProblemMessageSourceResolver.of(ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.SECURITY_ACCESS_DENIED,
            exception.getMessage()));
    return create(exception, request, HttpStatus.FORBIDDEN, problem);
  }
}
