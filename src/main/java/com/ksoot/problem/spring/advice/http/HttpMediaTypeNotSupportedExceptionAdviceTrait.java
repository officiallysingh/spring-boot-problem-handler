package com.ksoot.problem.spring.advice.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see HttpMediaTypeNotSupportedException
 * @see HttpStatus#UNSUPPORTED_MEDIA_TYPE
 */
public interface HttpMediaTypeNotSupportedExceptionAdviceTrait<T, R>
    extends BaseNotAcceptableAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleHttpMediaTypeNotSupportedException(
      final HttpMediaTypeNotSupportedException exception, final T request) {
    return processMediaTypeNotSupportedException(
        exception.getSupportedMediaTypes(), exception.getContentType(), exception, request);
  }
}
