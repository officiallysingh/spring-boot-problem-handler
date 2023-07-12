package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.core.Problem;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @see MethodArgumentNotValidException
 * @see BaseValidationAdviceTrait#defaultConstraintViolationStatus()
 */
public interface MethodArgumentNotValidAdviceTrait<T, R> extends BaseBindingResultHandlingAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMethodArgumentNotValid(final MethodArgumentNotValidException exception, final T request) {

    List<Problem> problems = handleBindingResult(exception.getBindingResult(), exception);

    return create(exception, request, defaultConstraintViolationStatus(),
        problems.toArray(new Problem[problems.size()]));
  }
}
