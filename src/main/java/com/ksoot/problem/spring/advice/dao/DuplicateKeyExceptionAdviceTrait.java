package com.ksoot.problem.spring.advice.dao;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see HttpStatus#INTERNAL_SERVER_ERROR
 */
public interface DuplicateKeyExceptionAdviceTrait<T, R> extends BaseDataIntegrityAdvice<T, R> {

  @ExceptionHandler
  default R handleDuplicateKeyException(final DuplicateKeyException exception, final T request) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    String exceptionMessage = exception.getMostSpecificCause().getMessage().trim();
    String constraintName = resolveConstraintName(exceptionMessage);

    String codeCode =
        ProblemConstant.CODE_CODE_PREFIX
            + GeneralErrorKey.DATA_INTEGRITY_VIOLATION
            + ProblemConstant.DOT
            + constraintName;
    String titleCode =
        ProblemConstant.TITLE_CODE_PREFIX
            + GeneralErrorKey.DATA_INTEGRITY_VIOLATION
            + ProblemConstant.DOT
            + constraintName;
    String messageCode =
        ProblemConstant.DETAIL_CODE_PREFIX
            + GeneralErrorKey.DATA_INTEGRITY_VIOLATION
            + ProblemConstant.DOT
            + constraintName;

    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(codeCode, status.value()),
            ProblemMessageSourceResolver.of(titleCode, status.getReasonPhrase()),
            ProblemMessageSourceResolver.of(
                messageCode, ProblemConstant.DB_CONSTRAINT_VIOLATION_DEFAULT_MESSAGE));

    return toResponse(exception, request, status, problem);
  }
}
