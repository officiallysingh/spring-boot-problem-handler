package com.pchf.problem.spring.advice.general;

import com.pchf.problem.core.DefaultProblem;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ThrowableProblem;
import com.pchf.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see Problem
 * @see ThrowableProblem
 */
public interface ProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleProblem(final ThrowableProblem problem, final T request) {
    return create(problem, request);
  }

  @ExceptionHandler
  default R handleProblem(final DefaultProblem problem, final T request) {
    return create(problem, request, HttpStatus.INTERNAL_SERVER_ERROR, problem);
  }
}
