package com.pchf.problem.spring.advice;

import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.core.ThrowableProblem;
import com.pchf.problem.core.ProblemUtils;
import com.pchf.problem.spring.config.ProblemBeanRegistry;
import com.pchf.problem.spring.config.ProblemMessageProvider;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * Advice traits are simple interfaces that provide a single method with a
 * default implementation. They are used to provide {@link ExceptionHandler}
 * implementations to be used in Spring Controllers and/or in a
 * {@link ControllerAdvice}. Clients can choose which traits they what to use à
 * la carte.
 * </p>
 * <p>
 * Advice traits are grouped in packages, based on they use cases. Every package
 * has a composite advice trait that bundles all traits of that package.
 * </p>
 *
 * @see ControllerAdvice
 * @see ExceptionHandler
 * @see Throwable
 * @see Exception
 * @see Problem
 */
public interface BaseAdviceTrait {

  default ThrowableProblem toProblem(final Throwable throwable) {
    HttpStatus status = HttpStatus.valueOf(ProblemUtils.resolveStatus(throwable).value());
    return toProblem(throwable, status);
  }

  default ThrowableProblem toProblem(final Throwable throwable, final HttpStatus status) {
    return toProblem(throwable, status, throwable.getMessage(), throwable.getMessage());
  }

  default ThrowableProblem toProblem(final Throwable throwable, final HttpStatus status, final String message,
                                     final String details) {
    return toProblem(throwable, "" + status.value(), status.getReasonPhrase(), message, details);
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code, final String title,
                                     final String message, final String details) {
    final ThrowableProblem problem = prepare(throwable, code, title, message, details, null);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code, final String title,
                                     final MessageSourceResolvable messageResolver, final MessageSourceResolvable detailsResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(2);
      parameters.put(ProblemConstant.MESSAGE_RESOLVER, messageResolver);
      parameters.put(ProblemConstant.DETAILS_RESOLVER, detailsResolver);
    }
    final ThrowableProblem problem = prepare(throwable, code, title,
        ProblemMessageProvider.getMessage(messageResolver), ProblemMessageProvider.getMessage(detailsResolver),
        parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable messageResolver,
                                     final MessageSourceResolvable detailsResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(3);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.MESSAGE_RESOLVER, messageResolver);
      parameters.put(ProblemConstant.DETAILS_RESOLVER, detailsResolver);
    }
    final ThrowableProblem problem = prepare(throwable, code, ProblemMessageProvider.getMessage(titleResolver),
        ProblemMessageProvider.getMessage(messageResolver), ProblemMessageProvider.getMessage(detailsResolver),
        parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final MessageSourceResolvable codeResolver,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable messageResolver,
                                     final MessageSourceResolvable detailsResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(4);
      parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.MESSAGE_RESOLVER, messageResolver);
      parameters.put(ProblemConstant.DETAILS_RESOLVER, detailsResolver);
    }
    final ThrowableProblem problem = prepare(throwable, ProblemMessageProvider.getMessage(codeResolver),
        ProblemMessageProvider.getMessage(titleResolver), ProblemMessageProvider.getMessage(messageResolver),
        ProblemMessageProvider.getMessage(detailsResolver), parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final MessageSourceResolvable codeResolver,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable messageResolver,
                                     final MessageSourceResolvable detailsResolver, final ProblemMessageSourceResolver statusResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(5);
      parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.MESSAGE_RESOLVER, messageResolver);
      parameters.put(ProblemConstant.DETAILS_RESOLVER, detailsResolver);
      parameters.put(ProblemConstant.STATUS_RESOLVER, statusResolver);
    }
    final ThrowableProblem problem = prepare(throwable, ProblemMessageProvider.getMessage(codeResolver),
        ProblemMessageProvider.getMessage(titleResolver), ProblemMessageProvider.getMessage(messageResolver),
        ProblemMessageProvider.getMessage(detailsResolver), parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code, final String title,
                                     final MessageSourceResolvable messageResolver, final String details) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(1);
      parameters.put(ProblemConstant.MESSAGE_RESOLVER, messageResolver);
    }
    final ThrowableProblem problem = prepare(throwable, code, title,
        ProblemMessageProvider.getMessage(messageResolver), details, parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final HttpStatus status,
                                     final MessageSourceResolvable messageResolver,
                                     final MessageSourceResolvable detailsResolver) {
    return toProblem(throwable, "" + status.value(), status.getReasonPhrase(), messageResolver, detailsResolver);
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable messageResolver,
                                     final String details) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(2);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.MESSAGE_RESOLVER, messageResolver);
    }
    final ThrowableProblem problem = prepare(throwable, code, ProblemMessageProvider.getMessage(titleResolver),
        ProblemMessageProvider.getMessage(messageResolver), details, parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code, final String title,
                                     final String message, final String details, final Map<String, Object> parameters) {
    final ThrowableProblem problem = prepare(throwable, code, title, message, details, parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem prepare(final Throwable throwable, final String code, final String title,
                                   final String message, final String details, final Map<String, Object> parameters) {
    return Problem.code(code).title(title).message(message).details(details)
        .cause(Optional.ofNullable(throwable.getCause())
            .filter(cause -> ProblemBeanRegistry.problemProperties().isCauseChainsEnabled())
            .map(this::toProblem).orElse(null))
        .parameters(parameters).build();
  }
}
