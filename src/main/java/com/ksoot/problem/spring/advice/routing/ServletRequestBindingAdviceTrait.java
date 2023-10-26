package com.ksoot.problem.spring.advice.routing;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see ServletRequestBindingException
 * @see HttpStatus#BAD_REQUEST
 */
public interface ServletRequestBindingAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleServletRequestBinding(
      final ServletRequestBindingException exception, final T request) {
    return toProblem(
        exception, request, GeneralErrorKey.INTERNAL_SERVER_ERROR, HttpStatus.BAD_REQUEST);
  }
}
