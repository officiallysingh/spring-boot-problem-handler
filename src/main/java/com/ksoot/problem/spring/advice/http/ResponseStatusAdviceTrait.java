package com.ksoot.problem.spring.advice.http;

import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

/**
 * Advice trait to handle {@link ResponseStatusException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see ResponseStatusException
 */
public interface ResponseStatusAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link ResponseStatusException} and converts it into a response.
   *
   * @param exception the exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleResponseStatusException(
      final ResponseStatusException exception, final T request) {
    return toResponse(exception, request, HttpStatus.valueOf(exception.getStatusCode().value()));
  }
}
