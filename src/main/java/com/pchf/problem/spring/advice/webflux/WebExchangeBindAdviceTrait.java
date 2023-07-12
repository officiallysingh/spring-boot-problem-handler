package com.pchf.problem.spring.advice.webflux;

import com.pchf.problem.core.Problem;
import com.pchf.problem.spring.advice.validation.BaseBindingResultHandlingAdviceTrait;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;

interface WebExchangeBindAdviceTrait<T, R> extends BaseBindingResultHandlingAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleWebExchangeBindException(final WebExchangeBindException exception, final T request) {
    final List<Problem> problems = handleBindingResult(exception.getBindingResult(), exception);
    return create(exception, request, defaultConstraintViolationStatus(),
        problems.toArray(new Problem[problems.size()]));
  }
}
