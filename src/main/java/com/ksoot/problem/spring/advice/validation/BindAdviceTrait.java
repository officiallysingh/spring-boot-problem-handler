package com.ksoot.problem.spring.advice.validation;

import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.VIOLATIONS_KEY;

import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_CODE_CODE_PREFIX),
            ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX),
            ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX),
            parameters);
    return toResponse(exception, request, defaultConstraintViolationStatus(), problem);
  }
}
