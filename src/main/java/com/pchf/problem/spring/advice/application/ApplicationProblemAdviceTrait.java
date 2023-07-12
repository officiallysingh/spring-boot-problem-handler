package com.pchf.problem.spring.advice.application;

import com.pchf.problem.core.ApplicationProblem;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.advice.BaseAdviceTrait;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see BaseAdviceTrait
 */
public interface ApplicationProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleApplicationProblem(final ApplicationProblem exception, final T request) {
    return create(exception, request, exception.status(), exception);
  }
}
