package com.pchf.problem.spring.advice.application;

import com.pchf.problem.core.MultiProblem;
import com.pchf.problem.core.Problem;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.advice.BaseAdviceTrait;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @see BaseAdviceTrait
 */
public interface ApplicationMultiProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleApplicationProblem(final MultiProblem exception,
                                     final T request) {
    List<Problem> problems = exception.getProblems();
    return create(exception, request, exception.getStatus(), problems.toArray(new Problem[problems.size()]));
  }
}
