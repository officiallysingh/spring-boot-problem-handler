package com.pchf.problem.spring.advice.routing;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Transforms {@link NoHandlerFoundException NoHandlerFoundExceptions} into
 * {@link HttpStatus#NOT_FOUND not-found} {@link Problem problems}.
 * <p>
 * <strong>Note</strong>: This requires
 * {@link DispatcherServlet#setThrowExceptionIfNoHandlerFound(boolean)} being
 * set to true.
 * </p>
 *
 * @see NoHandlerFoundException
 * @see HttpStatus#NOT_FOUND
 * @see DispatcherServlet#setThrowExceptionIfNoHandlerFound(boolean)
 */
public interface NoHandlerFoundAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleNoHandlerFound(final NoHandlerFoundException exception, final T request) {
    Problem problem = toProblem(exception,
        ProblemMessageSourceResolver.of(ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.NO_HANDLER_FOUND,
            HttpStatus.NOT_FOUND.value()),
        ProblemMessageSourceResolver.of(ProblemConstant.TITLE_CODE_PREFIX + GeneralErrorKey.NO_HANDLER_FOUND,
            HttpStatus.NOT_FOUND.getReasonPhrase()),
        ProblemMessageSourceResolver.of(ProblemConstant.MESSAGE_CODE_PREFIX + GeneralErrorKey.NO_HANDLER_FOUND,
            exception.getMessage()),
        ProblemMessageSourceResolver.of(ProblemConstant.DETAILS_CODE_PREFIX + GeneralErrorKey.NO_HANDLER_FOUND,
            exception.getMessage()));
    return create(exception, request, HttpStatus.NOT_FOUND, problem);
  }
}
