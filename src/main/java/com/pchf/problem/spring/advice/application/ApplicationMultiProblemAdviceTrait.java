package com.pchf.problem.spring.advice.application;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.MultiProblem;
import com.pchf.problem.core.Problem;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.advice.BaseAdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.pchf.problem.core.ProblemConstant.CODE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.DETAIL_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.ERRORS_KEY;
import static com.pchf.problem.core.ProblemConstant.TITLE_CODE_PREFIX;

/**
 * @see BaseAdviceTrait
 */
public interface ApplicationMultiProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleApplicationProblem(final MultiProblem exception,
                                     final T request) {
    List<Problem> problems = exception.getProblems();
    Map<String, Object> parameters = new LinkedHashMap<>(4);
    parameters.put(ERRORS_KEY, problems);

    HttpStatus status = HttpStatus.MULTI_STATUS;
    ProblemMessageSourceResolver codeResolver = ProblemMessageSourceResolver.of(CODE_CODE_PREFIX + GeneralErrorKey.MULTIPLE_ERRORS, status.value());
    ProblemMessageSourceResolver titleResolver = ProblemMessageSourceResolver.of(TITLE_CODE_PREFIX + GeneralErrorKey.MULTIPLE_ERRORS, status.getReasonPhrase());
    ProblemMessageSourceResolver detailResolver = ProblemMessageSourceResolver.of(DETAIL_CODE_PREFIX + GeneralErrorKey.MULTIPLE_ERRORS, exception.getMessage());

    Problem problem = toProblem(exception, codeResolver, titleResolver, detailResolver, parameters);
    return create(exception, request, exception.getStatus(), problem);
  }
}
