package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;

public interface BaseValidationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  default HttpStatus defaultConstraintViolationStatus() {
    return HttpStatus.BAD_REQUEST;
  }
}
