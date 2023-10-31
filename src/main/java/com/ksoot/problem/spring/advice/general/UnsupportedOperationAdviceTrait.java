package com.ksoot.problem.spring.advice.general;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see UnsupportedOperationException
 * @see HttpStatus#NOT_IMPLEMENTED
 */
public interface UnsupportedOperationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleUnsupportedOperation(
      final UnsupportedOperationException exception, final T request) {
    HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
    Problem problem = toProblem(exception, GeneralErrorKey.INTERNAL_SERVER_ERROR, status);
    return toResponse(exception, request, status, problem);
  }
}
