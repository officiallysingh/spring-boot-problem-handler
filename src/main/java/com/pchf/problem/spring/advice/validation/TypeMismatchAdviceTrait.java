package com.pchf.problem.spring.advice.validation;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see TypeMismatchException
 * @see HttpStatus#BAD_REQUEST
 */
public interface TypeMismatchAdviceTrait<T, R> extends BaseValidationAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleTypeMismatch(final TypeMismatchException exception, final T request) {

    HttpStatus status = defaultConstraintViolationStatus();

    String errorKey = exception.getErrorCode() + ProblemConstant.DOT + exception.getPropertyName();

    String codeCode = ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.TYPE_MISMATCH + ProblemConstant.DOT + errorKey;
    String titleCode = ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.TYPE_MISMATCH + ProblemConstant.DOT + errorKey;
    String messageCode = ProblemConstant.MESSAGE_CODE_PREFIX + GeneralErrorKey.TYPE_MISMATCH + ProblemConstant.DOT + errorKey;
    String detailsCode = ProblemConstant.DETAILS_CODE_PREFIX + GeneralErrorKey.TYPE_MISMATCH + ProblemConstant.DOT + errorKey;

    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(codeCode, status.value()),
        ProblemMessageSourceResolver.of(titleCode, status.getReasonPhrase()),
        ProblemMessageSourceResolver.of(messageCode, exception.getMostSpecificCause().toString()),
        ProblemMessageSourceResolver.of(detailsCode, exception.getMessage()));

    return create(exception, request, status, problem);
  }
}
