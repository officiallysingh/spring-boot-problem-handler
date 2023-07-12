package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.stream.Stream;

public interface BaseBindingResultHandlingAdviceTrait<T, R> extends BaseValidationAdviceTrait<T, R> {

  default List<Problem> handleBindingResult(final BindingResult bindingResult, final Throwable exception) {

    final Stream<Problem> fieldErrors = bindingResult.getFieldErrors().stream()
        .map(fieldError -> handleFieldError(fieldError, exception));

    final Stream<Problem> globalErrors = bindingResult.getGlobalErrors().stream()
        .map(objectError -> handleObjectError(objectError, exception));

    return Stream.concat(fieldErrors, globalErrors).toList();
  }

  default Problem handleFieldError(final FieldError fieldError, final Throwable exception) {

    HttpStatus status = defaultConstraintViolationStatus();

    ProblemMessageSourceResolver codeResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX, fieldError, status.value());
    ProblemMessageSourceResolver titleResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX, fieldError, status.getReasonPhrase());
    ProblemMessageSourceResolver messageResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_MESSAGE_CODE_PREFIX, fieldError);
    ProblemMessageSourceResolver detailsResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_DETAILS_CODE_PREFIX, fieldError);

    return toProblem(exception, codeResolver, titleResolver, messageResolver, detailsResolver);
  }

  default Problem handleObjectError(final ObjectError objectError, final Throwable exception) {

    HttpStatus status = defaultConstraintViolationStatus();

    ProblemMessageSourceResolver codeResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX, objectError, status.value());
    ProblemMessageSourceResolver titleResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX, objectError, status.getReasonPhrase());
    ProblemMessageSourceResolver messageResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_MESSAGE_CODE_PREFIX, objectError);
    ProblemMessageSourceResolver detailsResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CONSTRAINT_VIOLATION_DETAILS_CODE_PREFIX, objectError);

    return toProblem(exception, codeResolver, titleResolver, messageResolver, detailsResolver);
  }
}
