package com.pchf.problem.spring.advice.general;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see UnsupportedOperationException
 * @see HttpStatus#NOT_IMPLEMENTED
 */
public interface UnsupportedOperationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleUnsupportedOperation(final UnsupportedOperationException exception, final T request) {
    return toProblem(exception, request, GeneralErrorKey.INTERNAL_SERVER_ERROR, HttpStatus.NOT_IMPLEMENTED);
  }
}
