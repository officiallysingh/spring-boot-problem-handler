package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.core.Problem;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.pchf.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.VIOLATIONS_KEY;

/**
 * @see BindException
 * @see ViolationVM
 * @see BaseValidationAdviceTrait#defaultConstraintViolationStatus()
 */
public interface BindAdviceTrait<T, R> extends BaseBindingResultHandlingAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleBindException(final BindException exception, final T request) {
    List<ViolationVM> violations = handleBindingResult(exception.getBindingResult(), exception);
    Map<String, Object> parameters = new LinkedHashMap<>(4);
    parameters.put(VIOLATIONS_KEY, violations);
    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_CODE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX), parameters);
    return create(exception, request, defaultConstraintViolationStatus(),
        problem);
  }
}
