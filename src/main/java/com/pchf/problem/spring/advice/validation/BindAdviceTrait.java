package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.core.Problem;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @see BindException
 * @see Violation
 * @see ConstraintViolationProblem
 * @see ConstraintViolationProblem#TYPE_VALUE
 * @see BaseValidationAdviceTrait#defaultConstraintViolationStatus()
 */
public interface BindAdviceTrait<T, R> extends BaseBindingResultHandlingAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleBindException(final BindException exception, final T request) {

    List<Problem> problems = handleBindingResult(exception.getBindingResult(), exception);

    return create(exception, request, defaultConstraintViolationStatus(),
        problems.toArray(new Problem[problems.size()]));
  }
}
