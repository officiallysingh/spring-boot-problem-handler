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
 * Advice trait to handle {@link AuthenticationException}s. Specifically for use when authentication
 * is required and has failed or has not yet been provided.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see AuthenticationException
 * @see HttpStatus#UNAUTHORIZED
 */
public interface AuthenticationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link AuthenticationException} and converts it into a {@link Problem} response.
   *
   * @param exception the authentication exception
   * @param request the request
   * @return the error response
   */
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
