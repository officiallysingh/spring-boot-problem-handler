package com.ksoot.problem.spring.advice.general;

import com.ksoot.problem.core.DefaultProblem;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ThrowableProblem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see Problem
 * @see ThrowableProblem
 */
public interface ProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleProblem(final ThrowableProblem problem, final T request) {
    return toResponse(problem, request);
  }

  @ExceptionHandler
  default R handleProblem(final DefaultProblem problem, final T request) {
    return toResponse(problem, request, HttpStatus.INTERNAL_SERVER_ERROR, problem);
  }
}
