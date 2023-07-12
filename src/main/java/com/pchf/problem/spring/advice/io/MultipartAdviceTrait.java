package com.pchf.problem.spring.advice.io;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

// TODO find a better name
public interface MultipartAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMultipart(final MultipartException exception, final T request) {
    return toProblem(exception, request, GeneralErrorKey.INTERNAL_SERVER_ERROR, HttpStatus.BAD_REQUEST);
  }
}
