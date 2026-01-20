package com.ksoot.problem.spring.advice.general;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle all otherwise unhandled {@link Throwable}s and {@link Exception}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see Throwable
 * @see Exception
 */
public interface ThrowableAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link Throwable} and converts it into a {@link Problem} response.
   *
   * @param throwable the throwable
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleThrowable(final Throwable throwable, final T request) {
    HttpStatus status = resolveStatus(throwable);
    Problem problem = toProblem(throwable, GeneralErrorKey.INTERNAL_SERVER_ERROR, status);
    return toResponse(throwable, request, status, problem);
  }
}
