package com.ksoot.problem.spring.advice.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link HttpMediaTypeNotSupportedException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see HttpMediaTypeNotSupportedException
 * @see HttpStatus#UNSUPPORTED_MEDIA_TYPE
 */
public interface HttpMediaTypeNotSupportedExceptionAdviceTrait<T, R>
    extends BaseNotAcceptableAdviceTrait<T, R> {

  /**
   * Handles {@link HttpMediaTypeNotSupportedException} and converts it into a response.
   *
   * @param exception the exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleHttpMediaTypeNotSupportedException(
      final HttpMediaTypeNotSupportedException exception, final T request) {
    return processMediaTypeNotSupportedException(
        exception.getSupportedMediaTypes(), exception.getContentType(), exception, request);
  }
}
