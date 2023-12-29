package com.ksoot.problem.spring.advice;

import static com.ksoot.problem.core.ProblemConstant.CODE_RESOLVER;
import static com.ksoot.problem.core.ProblemConstant.DETAIL_RESOLVER;
import static com.ksoot.problem.core.ProblemConstant.STACKTRACE_KEY;
import static com.ksoot.problem.core.ProblemConstant.STATUS_RESOLVER;
import static com.ksoot.problem.core.ProblemConstant.TITLE_RESOLVER;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.core.ProblemUtils;
import com.ksoot.problem.core.Problems;
import com.ksoot.problem.core.ThrowableProblem;
import com.ksoot.problem.spring.config.ProblemBeanRegistry;
import com.ksoot.problem.spring.config.ProblemMessageProvider;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public interface AdviceTrait<T, R> {

  Logger logger = LoggerFactory.getLogger(AdviceTrait.class);

  default HttpStatus resolveStatus(final Throwable throwable) {
    HttpStatus defaultStatus = HttpStatus.valueOf(ProblemUtils.resolveStatus(throwable).value());
    String errorKey = ClassUtils.getName(throwable);
    ProblemMessageSourceResolver defaultStatusResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.STATUS_CODE_PREFIX + errorKey, defaultStatus.value());
    HttpStatus status = defaultStatus;
    try {
      int statusCode = Integer.parseInt(ProblemMessageProvider.getMessage(defaultStatusResolver));
      status = HttpStatus.valueOf(statusCode);
    } catch (final Exception e) {
      // Ignore on purpose
    }
    return status;
  }

  // ------ Create problem from exceptions ------
  default Problem toProblem(
      final Throwable throwable, final String defaultErrorKey, final HttpStatus status) {

    String errorKey = ClassUtils.getName(throwable);

    ProblemMessageSourceResolver defaultCodeResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.CODE_CODE_PREFIX + defaultErrorKey, status.value());
    ProblemMessageSourceResolver defaultTitleResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.TITLE_CODE_PREFIX + defaultErrorKey, status.getReasonPhrase());
    ProblemMessageSourceResolver defaultDetailResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.DETAIL_CODE_PREFIX + defaultErrorKey, throwable.getMessage());

    ProblemMessageSourceResolver codeResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.CODE_CODE_PREFIX + errorKey,
            ProblemMessageProvider.getMessage(defaultCodeResolver));
    ProblemMessageSourceResolver titleResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.TITLE_CODE_PREFIX + errorKey,
            ProblemMessageProvider.getMessage(defaultTitleResolver));
    ProblemMessageSourceResolver detailResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.DETAIL_CODE_PREFIX + errorKey,
            ProblemMessageProvider.getMessage(defaultDetailResolver));
    ProblemMessageSourceResolver statusResolver =
        ProblemMessageSourceResolver.of(
            ProblemConstant.STATUS_CODE_PREFIX + errorKey, status.value());

    Problem problem =
        toProblem(throwable, codeResolver, titleResolver, detailResolver, statusResolver);
    return problem;
  }

  default ThrowableProblem toProblem(final Throwable throwable) {
    HttpStatus status = HttpStatus.valueOf(ProblemUtils.resolveStatus(throwable).value());
    return toProblem(throwable, status);
  }

  default ThrowableProblem toProblem(final Throwable throwable, final HttpStatus status) {
    return toProblem(throwable, status, throwable.getMessage());
  }

  default ThrowableProblem toProblem(
      final Throwable throwable, final HttpStatus status, final String detail) {
    return toProblem(throwable, ProblemUtils.statusCode(status), status.getReasonPhrase(), detail);
  }

  default ThrowableProblem toProblem(
      final Throwable throwable, final String code, final String title, final String detail) {
    return toProblem(throwable, code, title, detail, new LinkedHashMap<>());
  }

  default ThrowableProblem toProblem(
      final Throwable throwable,
      final String code,
      final String title,
      final String detail,
      final Map<String, Object> parameters) {
    final ThrowableProblem problem = buildProblem(throwable, code, title, detail, parameters);
    final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
    problem.setStackTrace(stackTrace);
    return problem;
  }

  default ThrowableProblem toProblem(
      final Throwable throwable,
      final HttpStatus status,
      final MessageSourceResolvable detailResolver) {
    return toProblem(
        throwable, ProblemUtils.statusCode(status), status.getReasonPhrase(), detailResolver);
  }

  default ThrowableProblem toProblem(
      final Throwable throwable,
      final String code,
      final String title,
      final MessageSourceResolvable detailResolver) {
    Map<String, Object> parameters = new LinkedHashMap<>();
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters.put(DETAIL_RESOLVER, detailResolver);
    }
    return toProblem(
        throwable, code, title, ProblemMessageProvider.getMessage(detailResolver), parameters);
  }

  default ThrowableProblem toProblem(
      final Throwable throwable,
      final String code,
      final MessageSourceResolvable titleResolver,
      final MessageSourceResolvable detailResolver) {
    Map<String, Object> parameters = new LinkedHashMap<>();
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters.put(TITLE_RESOLVER, titleResolver);
      parameters.put(DETAIL_RESOLVER, detailResolver);
    }
    return toProblem(
        throwable,
        code,
        ProblemMessageProvider.getMessage(titleResolver),
        ProblemMessageProvider.getMessage(detailResolver),
        parameters);
  }

  default ThrowableProblem toProblem(
      final Throwable throwable,
      final MessageSourceResolvable codeResolver,
      final MessageSourceResolvable titleResolver,
      final MessageSourceResolvable detailResolver) {
    return toProblem(throwable, codeResolver, titleResolver, detailResolver, new LinkedHashMap<>());
  }

  default ThrowableProblem toProblem(
      final Throwable throwable,
      final MessageSourceResolvable codeResolver,
      final MessageSourceResolvable titleResolver,
      final MessageSourceResolvable detailResolver,
      final Map<String, Object> parameters) {
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters.put(CODE_RESOLVER, codeResolver);
      parameters.put(TITLE_RESOLVER, titleResolver);
      parameters.put(DETAIL_RESOLVER, detailResolver);
    }
    return toProblem(
        throwable,
        ProblemMessageProvider.getMessage(codeResolver),
        ProblemMessageProvider.getMessage(titleResolver),
        ProblemMessageProvider.getMessage(detailResolver),
        parameters);
  }

  default ThrowableProblem toProblem(
      final Throwable throwable,
      final MessageSourceResolvable codeResolver,
      final MessageSourceResolvable titleResolver,
      final MessageSourceResolvable detailResolver,
      final MessageSourceResolvable statusResolver) {
    Map<String, Object> parameters = new LinkedHashMap<>();
    if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
      parameters.put(CODE_RESOLVER, codeResolver);
      parameters.put(TITLE_RESOLVER, titleResolver);
      parameters.put(DETAIL_RESOLVER, detailResolver);
      parameters.put(STATUS_RESOLVER, statusResolver);
    }
    return toProblem(
        throwable,
        ProblemMessageProvider.getMessage(codeResolver),
        ProblemMessageProvider.getMessage(titleResolver),
        ProblemMessageProvider.getMessage(detailResolver),
        parameters);
  }

  default Problem toProblem(
      final Throwable exception,
      final HttpStatus status,
      final String errorKey,
      final String defaultDetail,
      final Object[] detailArgs,
      final Map<String, Object> parameters) {
    String codeCode = ProblemConstant.CODE_CODE_PREFIX + errorKey;
    String titleCode = ProblemConstant.TITLE_CODE_PREFIX + errorKey;
    String detailCode = ProblemConstant.DETAIL_CODE_PREFIX + errorKey;

    return toProblem(
        exception,
        ProblemMessageSourceResolver.of(codeCode, status.value()),
        ProblemMessageSourceResolver.of(titleCode, status.getReasonPhrase()),
        ProblemMessageSourceResolver.of(
            detailCode, Optional.ofNullable(defaultDetail).orElse(detailCode), detailArgs),
        Optional.ofNullable(parameters).orElse(Collections.emptyMap()));
  }

  default ThrowableProblem buildProblem(
      final Throwable throwable,
      final String code,
      final String title,
      final String detail,
      final Map<String, Object> parameters) {
    if (ProblemBeanRegistry.problemProperties().isStacktraceEnabled()) {
      final StackTraceElement[] stackTrace = ProblemUtils.createStackTrace(throwable);
      parameters.put(STACKTRACE_KEY, stackTrace);
    }
    return Problems.newInstance(code, title, detail)
        .cause(
            Optional.ofNullable(throwable.getCause())
                .filter(cause -> ProblemBeanRegistry.problemProperties().isCauseChainsEnabled())
                .map(this::toProblem)
                .orElse(null))
        .parameters(Collections.unmodifiableMap(parameters))
        .build();
  }

  // ------ Create Error response from Problems ------
  default R toResponse(final Throwable throwable, final T request) {
    HttpStatus status = HttpStatus.valueOf(ProblemUtils.resolveStatus(throwable).value());
    return toResponse(throwable, request, status, toProblem(throwable));
  }

  default R toResponse(final Throwable throwable, final T request, final HttpStatus status) {
    return toResponse(throwable, request, status, new HttpHeaders());
  }

  default R toResponse(
      final Throwable throwable,
      final T request,
      final HttpStatus status,
      final HttpHeaders headers) {
    return buildResponse(throwable, request, status, headers, toProblem(throwable, status));
  }

  default R toResponse(
      final Throwable throwable, final T request, final HttpStatus status, final Problem problem) {
    return buildResponse(throwable, request, status, new HttpHeaders(), problem);
  }

  default R buildResponse(
      final Throwable throwable,
      final T request,
      final HttpStatus status,
      final HttpHeaders headers,
      final Problem problem) {
    log(throwable, status);
    return errorResponseBuilder().buildResponse(throwable, request, status, headers, problem);
  }

  default void log(final Throwable throwable, final HttpStatus status) {
    logger.error(status.getReasonPhrase(), throwable);
    // throwable.printStackTrace();
  }

  // default void log(final Throwable throwable, final HttpStatus status) {
  // if (status.is4xxClientError()) {
  // logger.warn("{}: {}", status.getReasonPhrase(), throwable.getMessage());
  // } else if (status.is5xxServerError()) {
  // logger.error(status.getReasonPhrase(), throwable);
  // }
  // }

  // ErrorResponseBuilder can have different implementations as per consumer needs, So
  // can be overridden.
  // Would be different for Web or Webflux applications
  default ErrorResponseBuilder<T, R> errorResponseBuilder() {
    return ProblemBeanRegistry.errorResponseBuilder();
  }
}
