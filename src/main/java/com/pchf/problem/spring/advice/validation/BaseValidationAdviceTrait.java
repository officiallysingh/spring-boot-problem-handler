package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemBeanRegistry;
import com.pchf.problem.spring.config.ProblemMessageProvider;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;

public interface BaseValidationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  default HttpStatus defaultConstraintViolationStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  default ViolationVM createViolation(final ProblemMessageSourceResolver codeResolver,
                                      final ProblemMessageSourceResolver messageResolver,
                                      final String propertyPath) {
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      return ViolationVM.of(ProblemMessageProvider.getMessage(codeResolver),
          ProblemMessageProvider.getMessage(messageResolver),
          propertyPath, codeResolver, messageResolver);
    } else {
      return ViolationVM.of(ProblemMessageProvider.getMessage(codeResolver),
          ProblemMessageProvider.getMessage(messageResolver),
          propertyPath);
    }
  }
}
