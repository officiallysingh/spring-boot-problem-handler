package com.ksoot.problem.spring.advice.http;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

/**
 * Advice trait to handle {@link UnsupportedMediaTypeStatusException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see UnsupportedMediaTypeStatusException
 * @see org.springframework.http.HttpStatus#UNSUPPORTED_MEDIA_TYPE
 */
public interface UnsupportedMediaTypeStatusAdviceTrait<T, R>
    extends BaseNotAcceptableAdviceTrait<T, R> {

  /**
   * Handles {@link UnsupportedMediaTypeStatusException} and converts it into a response.
   *
   * @param exception the exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleUnsupportedMediaTypeStatusException(
      final UnsupportedMediaTypeStatusException exception, final T request) {
    return processMediaTypeNotSupportedException(
        exception.getSupportedMediaTypes(), exception.getContentType(), exception, request);
  }
}
