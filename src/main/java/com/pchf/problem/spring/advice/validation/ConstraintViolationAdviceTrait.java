package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @see ConstraintViolationException
 * @see BaseValidationAdviceTrait#defaultConstraintViolationStatus()
 */
public interface ConstraintViolationAdviceTrait<T, R> extends BaseValidationAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleConstraintViolationException(final ConstraintViolationException exception, final T request) {
    final List<Problem> problems = exception.getConstraintViolations().stream()
        .map(violation -> handleConstraintViolation(violation, exception)).toList();

    return create(exception, request, defaultConstraintViolationStatus(),
        problems.toArray(new Problem[problems.size()]));
  }

  @SuppressWarnings("rawtypes")
  default Problem handleConstraintViolation(final ConstraintViolation violation,
                                            final ConstraintViolationException exception) {

    HttpStatus status = defaultConstraintViolationStatus();

    ProblemMessageSourceResolver codeResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX, violation, status.value());
    ProblemMessageSourceResolver titleResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX, violation, status.getReasonPhrase());
    ProblemMessageSourceResolver messageResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_MESSAGE_CODE_PREFIX, violation);
    ProblemMessageSourceResolver detailsResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_DETAILS_CODE_PREFIX, violation);

    return toProblem(exception, codeResolver, titleResolver, messageResolver, detailsResolver);

  }
}
