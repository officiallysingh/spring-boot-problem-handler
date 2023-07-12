package com.pchf.problem.spring.advice.dao;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
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

    String codeCode = ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.DATA_INTEGRITY_VIOLATION + ProblemConstant.DOT + constraintName;
    String titleCode = ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.DATA_INTEGRITY_VIOLATION + ProblemConstant.DOT + constraintName;
    String messageCode = ProblemConstant.MESSAGE_CODE_PREFIX + GeneralErrorKey.DATA_INTEGRITY_VIOLATION + ProblemConstant.DOT + constraintName;
    String detailsCode = ProblemConstant.DETAILS_CODE_PREFIX + GeneralErrorKey.DATA_INTEGRITY_VIOLATION + ProblemConstant.DOT + constraintName;

    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(codeCode, status.value()),
        ProblemMessageSourceResolver.of(titleCode, status.getReasonPhrase()),
        ProblemMessageSourceResolver.of(messageCode, ProblemConstant.DB_CONSTRAINT_VIOLATION_DEFAULT_MESSAGE),
        ProblemMessageSourceResolver.of(detailsCode, exception.getMessage()));

    return create(exception, request, status, problem);
  }
}
