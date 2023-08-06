package com.ksoot.problem.spring.advice.dao;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see HttpStatus#INTERNAL_SERVER_ERROR
 */
public interface DataIntegrityViolationAdviceTrait<T, R> extends BaseDataIntegrityAdvice<T, R> {

  @ExceptionHandler
  default R handleDataIntegrityViolationException(final DataIntegrityViolationException exception, final T request) {

    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    String exceptionMessage = exception.getMostSpecificCause().getMessage().trim();
    String constraintName = resolveConstraintName(exceptionMessage);

    String codeCode = ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.DATA_INTEGRITY_VIOLATION + ProblemConstant.DOT + constraintName;
    String titleCode = ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.DATA_INTEGRITY_VIOLATION + ProblemConstant.DOT + constraintName;
    String detailCode = ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.DATA_INTEGRITY_VIOLATION + ProblemConstant.DOT + constraintName;

    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(codeCode, status.value()),
        ProblemMessageSourceResolver.of(titleCode, status.getReasonPhrase()),
        ProblemMessageSourceResolver.of(detailCode, ProblemConstant.DB_CONSTRAINT_VIOLATION_DEFAULT_MESSAGE));

    return create(exception, request, status, problem);
  }
}
