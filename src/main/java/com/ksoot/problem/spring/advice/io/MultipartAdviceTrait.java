package com.ksoot.problem.spring.advice.io;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

// TODO find a better name
public interface MultipartAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMultipart(final MultipartException exception, final T request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    Problem problem = toProblem(exception, GeneralErrorKey.INTERNAL_SERVER_ERROR, status);
    return toResponse(exception, request, status, problem);
  }
}
