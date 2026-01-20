package com.ksoot.problem.spring.advice.routing;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Advice trait to handle {@link NoHandlerFoundException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see NoHandlerFoundException
 * @see HttpStatus#NOT_FOUND
 */
public interface NoHandlerFoundAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link NoHandlerFoundException} and converts it into a {@link Problem} response.
   *
   * @param exception the no handler found exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleNoHandlerFound(final NoHandlerFoundException exception, final T request) {
    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(
                ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.NO_HANDLER_FOUND,
                HttpStatus.NOT_FOUND.value()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.NO_HANDLER_FOUND,
                HttpStatus.NOT_FOUND.getReasonPhrase()),
            ProblemMessageSourceResolver.of(
                ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.NO_HANDLER_FOUND,
                exception.getMessage()));
    return toResponse(exception, request, HttpStatus.NOT_FOUND, problem);
  }
}
