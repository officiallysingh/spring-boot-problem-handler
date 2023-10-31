package com.ksoot.problem.spring.advice.http;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.NotAcceptableStatusException;

/**
 * @see NotAcceptableStatusException
 * @see HttpStatus#NOT_ACCEPTABLE
 */
public interface NotAcceptableStatusAdviceTrait<T, R> extends BaseNotAcceptableAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMediaTypeNotAcceptable(
      final NotAcceptableStatusException exception, final T request) {
    HttpStatus status = HttpStatus.NOT_ACCEPTABLE;
    Problem problem = toProblem(exception, GeneralErrorKey.INTERNAL_SERVER_ERROR, status);
    return toResponse(exception, request, status, problem);
  }
}
