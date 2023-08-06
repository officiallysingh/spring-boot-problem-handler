package com.ksoot.problem.spring.advice.http;

import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * @see ResponseStatusException
 */
public interface ResponseStatusAdviceTrait<T, R> extends AdviceTrait<T, R> /* SpringAdviceTrait<T, R> */ {

  @ExceptionHandler
  default R handleResponseStatusException(final ResponseStatusException exception, final T request) {
    return create(exception, request, HttpStatus.valueOf(exception.getStatusCode().value()));
  }
}
