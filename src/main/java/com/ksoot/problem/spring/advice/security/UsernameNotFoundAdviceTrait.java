package com.ksoot.problem.spring.advice.security;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link UsernameNotFoundException}s. Specifically for use when
 * authentication is required and has failed or has not yet been provided.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see UsernameNotFoundException
 * @see HttpStatus#UNAUTHORIZED
 */
public interface UsernameNotFoundAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link UsernameNotFoundException} and converts it into a {@link Problem} response.
   *
   * @param exception the authentication exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleAuthenticationException(
      final UsernameNotFoundException exception, final T request) {
    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(
                ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.SECURITY_INVALID_CREDENTIALS,
                HttpStatus.NOT_FOUND.value()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.SECURITY_INVALID_CREDENTIALS,
                HttpStatus.NOT_FOUND.getReasonPhrase()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.SECURITY_INVALID_CREDENTIALS,
                exception.getMessage()));
    return toResponse(exception, request, HttpStatus.NOT_FOUND, problem);
  }
}
