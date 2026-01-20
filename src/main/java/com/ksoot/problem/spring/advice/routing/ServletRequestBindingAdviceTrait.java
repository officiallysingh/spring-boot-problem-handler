package com.ksoot.problem.spring.advice.routing;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link ServletRequestBindingException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see ServletRequestBindingException
 * @see HttpStatus#BAD_REQUEST
 */
public interface ServletRequestBindingAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link ServletRequestBindingException} and converts it into a {@link Problem} response.
   *
   * @param exception the servlet request binding exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleServletRequestBinding(
      final ServletRequestBindingException exception, final T request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;
    Problem problem = toProblem(exception, GeneralErrorKey.INTERNAL_SERVER_ERROR, status);
    return toResponse(exception, request, status, problem);
  }
}
