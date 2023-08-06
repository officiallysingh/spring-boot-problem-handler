package com.ksoot.problem.spring.advice.webflux;

import com.ksoot.problem.spring.advice.validation.BaseBindingResultHandlingAdviceTrait;
import com.ksoot.problem.spring.advice.validation.ViolationVM;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.ERRORS_KEY;

interface WebExchangeBindAdviceTrait<T, R> extends BaseBindingResultHandlingAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleWebExchangeBindException(final WebExchangeBindException exception, final T request) {
    final List<ViolationVM> violations = handleBindingResult(exception.getBindingResult(), exception);
    Map<String, Object> parameters = new LinkedHashMap<>(4);
    parameters.put(ERRORS_KEY, violations);
    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_CODE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX), parameters);
    return create(exception, request, defaultConstraintViolationStatus(),
        problem);
  }
}
