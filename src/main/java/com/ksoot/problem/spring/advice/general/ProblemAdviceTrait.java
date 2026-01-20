package com.ksoot.problem.spring.advice.general;

import com.ksoot.problem.core.DefaultProblem;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ThrowableProblem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link ThrowableProblem}s and {@link DefaultProblem}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see Problem
 * @see ThrowableProblem
 * @see DefaultProblem
 */
public interface ProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link ThrowableProblem} and converts it into a response.
   *
   * @param problem the problem
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleProblem(final ThrowableProblem problem, final T request) {
    return toResponse(problem, request);
  }

  /**
   * Handles {@link DefaultProblem} and converts it into a response.
   *
   * @param problem the default problem
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleProblem(final DefaultProblem problem, final T request) {
    return toResponse(problem, request, HttpStatus.INTERNAL_SERVER_ERROR, problem);
  }
}
