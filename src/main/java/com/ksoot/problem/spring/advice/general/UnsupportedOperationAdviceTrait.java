package com.ksoot.problem.spring.advice.general;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link UnsupportedOperationException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see UnsupportedOperationException
 * @see HttpStatus#NOT_IMPLEMENTED
 */
public interface UnsupportedOperationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link UnsupportedOperationException} and converts it into a {@link Problem} response
   * with {@link HttpStatus#NOT_IMPLEMENTED} status.
   *
   * @param exception the unsupported operation exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleUnsupportedOperation(
      final UnsupportedOperationException exception, final T request) {
    HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
    Problem problem = toProblem(exception, GeneralErrorKey.INTERNAL_SERVER_ERROR, status);
    return toResponse(exception, request, status, problem);
  }
}
