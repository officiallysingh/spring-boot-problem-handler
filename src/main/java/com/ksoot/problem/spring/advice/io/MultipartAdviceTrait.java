package com.ksoot.problem.spring.advice.io;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

/**
 * Advice trait to handle {@link MultipartException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see MultipartException
 * @see HttpStatus#BAD_REQUEST
 */
public interface MultipartAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link MultipartException} and converts it into a response.
   *
   * @param exception the multipart exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleMultipart(final MultipartException exception, final T request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    Problem problem = toProblem(exception, GeneralErrorKey.INTERNAL_SERVER_ERROR, status);
    return toResponse(exception, request, status, problem);
  }
}
