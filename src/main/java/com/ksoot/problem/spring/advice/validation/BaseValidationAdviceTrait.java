package com.ksoot.problem.spring.advice.validation;

import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemBeanRegistry;
import com.ksoot.problem.spring.config.ProblemMessageProvider;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;

public interface BaseValidationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  default HttpStatus defaultConstraintViolationStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  default ViolationVM createViolation(
      final ProblemMessageSourceResolver codeResolver,
      final ProblemMessageSourceResolver messageResolver,
      final String propertyPath) {
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      return ViolationVM.of(
          ProblemMessageProvider.getMessage(codeResolver),
          ProblemMessageProvider.getMessage(messageResolver),
          propertyPath,
          codeResolver,
          messageResolver);
    } else {
      return ViolationVM.of(
          ProblemMessageProvider.getMessage(codeResolver),
          ProblemMessageProvider.getMessage(messageResolver),
          propertyPath);
    }
  }
}
