package com.ksoot.problem.spring.advice.validation;

import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.VIOLATIONS_KEY;

import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see ConstraintViolationException
 * @see BaseValidationAdviceTrait#defaultConstraintViolationStatus()
 */
public interface ConstraintViolationAdviceTrait<T, R> extends BaseValidationAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleConstraintViolationException(
      final ConstraintViolationException exception, final T request) {
    final List<ViolationVM> violations =
        exception.getConstraintViolations().stream()
            .map(violation -> handleConstraintViolation(violation, exception))
            .toList();
    Map<String, Object> parameters = new LinkedHashMap<>(4);
    parameters.put(VIOLATIONS_KEY, violations);
    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_CODE_CODE_PREFIX),
            ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX),
            ProblemMessageSourceResolver.of(
                CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX, exception.getMessage()),
            parameters);
    return toResponse(exception, request, defaultConstraintViolationStatus(), problem);
  }

  @SuppressWarnings("rawtypes")
  default ViolationVM handleConstraintViolation(
      final ConstraintViolation violation, final ConstraintViolationException exception) {
    HttpStatus status = defaultConstraintViolationStatus();
    ProblemMessageSourceResolver codeResolver =
        ProblemMessageSourceResolver.of(
            CONSTRAINT_VIOLATION_CODE_CODE_PREFIX, violation, status.name());
    ProblemMessageSourceResolver messageResolver =
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX, violation);
    return createViolation(codeResolver, messageResolver, violation.getPropertyPath().toString());
  }
}
