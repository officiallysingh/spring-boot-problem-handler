package com.pchf.problem.spring.advice.http;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

public interface UnsupportedMediaTypeStatusAdviceTrait<T, R> extends BaseNotAcceptableAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleUnsupportedMediaTypeStatusException(final UnsupportedMediaTypeStatusException exception, final T request) {
    return processMediaTypeNotSupportedException(exception.getSupportedMediaTypes(),
        exception.getContentType(), exception, request);
  }
}
