package com.ksoot.problem.spring.advice.routing;

import com.ksoot.problem.spring.advice.validation.BaseValidationAdviceTrait;
import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see MissingServletRequestParameterException
 * @see HttpStatus#BAD_REQUEST
 */
public interface MissingRequestHeaderAdviceTrait<T, R> extends BaseValidationAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMissingServletRequestParameter(final MissingRequestHeaderException exception, final T request) {
    String errorKey = exception.getParameter().getContainingClass().getSimpleName() + ProblemConstant.DOT
        + exception.getParameter().getMethod().getName() + ProblemConstant.DOT + exception.getHeaderName();

    String codeCode = ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.MISSING_REQUEST_HEADER + ProblemConstant.DOT + errorKey;
    String titleCode = ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.MISSING_REQUEST_HEADER + ProblemConstant.DOT + errorKey;
    String detailCode = ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.MISSING_REQUEST_HEADER + ProblemConstant.DOT + errorKey;

    HttpStatus status = defaultConstraintViolationStatus();

    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(codeCode, status.value()),
        ProblemMessageSourceResolver.of(titleCode, status.getReasonPhrase()),
        ProblemMessageSourceResolver.of(detailCode, exception.getMessage()));

    return create(exception, request, status, problem);
  }

}
