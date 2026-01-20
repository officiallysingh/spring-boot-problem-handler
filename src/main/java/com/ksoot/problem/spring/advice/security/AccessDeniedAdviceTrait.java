package com.ksoot.problem.spring.advice.security;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link AccessDeniedException}s. The request was a valid request, but the
 * server is refusing to respond to it. The user might be logged in but does not have the necessary
 * permissions for the resource.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see AccessDeniedException
 * @see HttpStatus#FORBIDDEN
 */
public interface AccessDeniedAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link AccessDeniedException} and converts it into a {@link Problem} response.
   *
   * @param exception the access denied exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleAccessDeniedException(final AccessDeniedException exception, final T request) {
    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(
                ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.SECURITY_ACCESS_DENIED,
                HttpStatus.FORBIDDEN.value()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.SECURITY_ACCESS_DENIED,
                HttpStatus.FORBIDDEN.getReasonPhrase()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.SECURITY_ACCESS_DENIED,
                exception.getMessage()));
    return toResponse(exception, request, HttpStatus.FORBIDDEN, problem);
  }
}
