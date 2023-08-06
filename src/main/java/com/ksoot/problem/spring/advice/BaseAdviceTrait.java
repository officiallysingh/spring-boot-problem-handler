package com.ksoot.problem.spring.advice;

import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.core.ProblemUtils;
import com.ksoot.problem.spring.config.ProblemBeanRegistry;
import com.ksoot.problem.spring.config.ProblemMessageProvider;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ThrowableProblem;
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
    return toProblem(throwable, status, throwable.getMessage());
  }

  default ThrowableProblem toProblem(final Throwable throwable, final HttpStatus status, final String detail) {
    return toProblem(throwable, String.valueOf(status.value()), status.getReasonPhrase(), detail);
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code, final String title,
                                     final String detail) {
    final ThrowableProblem problem = prepare(throwable, code, title, detail, null);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final HttpStatus status,
                                     final MessageSourceResolvable detailResolver) {
    return toProblem(throwable, String.valueOf(status.value()), status.getReasonPhrase(), detailResolver);
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code, final String title,
                                     final MessageSourceResolvable detailResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(1);
      parameters.put(ProblemConstant.DETAIL_RESOLVER, detailResolver);
    }
    final ThrowableProblem problem = prepare(throwable, code, title,
        ProblemMessageProvider.getMessage(detailResolver), parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable detailResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(2);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.DETAIL_RESOLVER, detailResolver);
    }
    final ThrowableProblem problem = prepare(throwable, code, ProblemMessageProvider.getMessage(titleResolver),
        ProblemMessageProvider.getMessage(detailResolver), parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final MessageSourceResolvable codeResolver,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable detailResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(3);
      parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.DETAIL_RESOLVER, detailResolver);
    }
    final ThrowableProblem problem = prepare(throwable, ProblemMessageProvider.getMessage(codeResolver),
        ProblemMessageProvider.getMessage(titleResolver), ProblemMessageProvider.getMessage(detailResolver), parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final MessageSourceResolvable codeResolver,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable detailResolver,
                                     final Map<String, Object> parameters) {
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.DETAIL_RESOLVER, detailResolver);
    }
    final ThrowableProblem problem = prepare(throwable, ProblemMessageProvider.getMessage(codeResolver),
        ProblemMessageProvider.getMessage(titleResolver), ProblemMessageProvider.getMessage(detailResolver), parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final MessageSourceResolvable codeResolver,
                                     final MessageSourceResolvable titleResolver, final MessageSourceResolvable detailResolver,
                                     final ProblemMessageSourceResolver statusResolver) {
    Map<String, Object> parameters = null;
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters = new LinkedHashMap<>(4);
      parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
      parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
      parameters.put(ProblemConstant.DETAIL_RESOLVER, detailResolver);
      parameters.put(ProblemConstant.STATUS_RESOLVER, statusResolver);
    }
    final ThrowableProblem problem = prepare(throwable, ProblemMessageProvider.getMessage(codeResolver),
        ProblemMessageProvider.getMessage(titleResolver), ProblemMessageProvider.getMessage(detailResolver),
        parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable, final String code, final String title,
                                     final String detail, final Map<String, Object> parameters) {
    final ThrowableProblem problem = prepare(throwable, code, title, detail, parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem prepare(final Throwable throwable, final String code, final String title,
                                   final String detail, final Map<String, Object> parameters) {
    return Problem.code(code).title(title).detail(detail)
        .cause(Optional.ofNullable(throwable.getCause())
            .filter(cause -> ProblemBeanRegistry.problemProperties().isCauseChainsEnabled())
            .map(this::toProblem).orElse(null))
        .parameters(parameters).build();
  }
}
