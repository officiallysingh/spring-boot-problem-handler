package com.ksoot.problem.spring.advice.validation;

import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemBeanRegistry;
import com.ksoot.problem.spring.config.ProblemMessageProvider;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;

/**
 * Base advice trait for validation-related exceptions.
 *
 * @param <T> the request type
 * @param <R> the response type
 */
public interface BaseValidationAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Returns the default HTTP status for constraint violations.
   *
   * @return {@link HttpStatus#BAD_REQUEST}
   */
  default HttpStatus defaultConstraintViolationStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  /**
   * Creates a {@link ViolationVM} instance.
   *
   * @param codeResolver the code resolver
   * @param messageResolver the message resolver
   * @param propertyPath the property path
   * @return the violation view model
   */
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
