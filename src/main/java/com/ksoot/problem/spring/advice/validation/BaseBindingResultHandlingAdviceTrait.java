package com.ksoot.problem.spring.advice.validation;

import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public interface BaseBindingResultHandlingAdviceTrait<T, R>
    extends BaseValidationAdviceTrait<T, R> {

  default List<ViolationVM> handleBindingResult(
      final BindingResult bindingResult, final Throwable exception) {

    final Stream<ViolationVM> fieldErrors =
        bindingResult.getFieldErrors().stream()
            .map(fieldError -> handleFieldError(fieldError, exception));

    final Stream<ViolationVM> globalErrors =
        bindingResult.getGlobalErrors().stream()
            .map(objectError -> handleObjectError(objectError, exception));

    return Stream.concat(fieldErrors, globalErrors).toList();
  }

  default ViolationVM handleFieldError(final FieldError fieldError, final Throwable exception) {
    HttpStatus status = defaultConstraintViolationStatus();
    ProblemMessageSourceResolver codeResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX, fieldError, status.value());
    ProblemMessageSourceResolver messageResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX, fieldError);
    return createViolation(codeResolver, messageResolver, fieldError.getField());
  }

  default ViolationVM handleObjectError(final ObjectError objectError, final Throwable exception) {
    HttpStatus status = defaultConstraintViolationStatus();
    ProblemMessageSourceResolver codeResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX, objectError, status.value());
    ProblemMessageSourceResolver messageResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX, objectError);
    return createViolation(codeResolver, messageResolver, objectError.getObjectName());
  }
}
