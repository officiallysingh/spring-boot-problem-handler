package com.ksoot.problem.spring.advice;

import com.ksoot.problem.spring.advice.application.ApplicationProblemAdviceTrait;
import com.ksoot.problem.spring.advice.general.GeneralAdviceTraits;
import com.ksoot.problem.spring.advice.http.HttpAdviceTraits;
import com.ksoot.problem.spring.advice.network.NetworkAdviceTraits;
import com.ksoot.problem.spring.advice.routing.RoutingAdviceTraits;
import com.ksoot.problem.spring.advice.validation.ValidationAdviceTraits;
import com.ksoot.problem.spring.advice.webflux.ProblemHandlingWebflux;
import com.ksoot.problem.spring.config.ProblemBeanRegistry;
import com.ksoot.problem.spring.config.ProblemMessageProvider;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.ProblemUtils;
import com.ksoot.problem.spring.advice.io.IOAdviceTraits;
import org.apache.commons.lang3.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <p>
 * Advice traits are simple interfaces that provide a single method with a
 * default implementation. They are used to provide {@link ExceptionHandler}
 * implementations to be used in {@link Controller Controllers} and/or in a
 * {@link ControllerAdvice}. Clients can choose which traits they what to use à
 * la carte.
 * </p>
 * <p>
 * Advice traits are grouped in packages, based on they use cases. Every package
 * has a composite advice trait that bundles all traits of that package.
 * Additionally there is one {@link ProblemHandlingWebflux major composite
 * advice trait} that in turn bundles all other composites.
 * </p>
 *
 * @see ControllerAdvice
 * @see ExceptionHandler
 * @see Throwable
 * @see Exception
 * @see Problem
 * @see ProblemHandlingWebflux
 * @see ApplicationProblemAdviceTrait
 * @see GeneralAdviceTraits
 * @see HttpAdviceTraits
 * @see IOAdviceTraits
 * @see NetworkAdviceTraits
 * @see RoutingAdviceTraits
 * @see ValidationAdviceTraits
 */
public interface AdviceTrait<T, R> extends BaseAdviceTrait {

  Logger logger = LoggerFactory.getLogger(AdviceTrait.class);

  default R create(final Throwable throwable, final T request) {
    HttpStatus status = HttpStatus.valueOf(ProblemUtils.resolveStatus(throwable).value());
    return create(throwable, request, status, toProblem(throwable));
  }

  default R create(final Throwable throwable, final T request, final HttpStatus status) {
    return create(throwable, request, status, new HttpHeaders());
  }

  default R create(final Throwable throwable, final T request, final HttpStatus status, final HttpHeaders headers) {
    return create(throwable, request, status, headers, toProblem(throwable, status));
  }

  default R create(final Throwable throwable, final T request, final HttpStatus status, final Problem problem) {
    return create(throwable, request, status, new HttpHeaders(), problem);
  }

  default R create(final Throwable throwable, final T request, final HttpStatus status, final HttpHeaders headers,
                   final Problem problem) {
    log(throwable, status);
    return errorResponseBuilder().buildResponse(throwable, request, status, headers, problem);
  }

  default R toProblem(final Throwable throwable, final T request) {
    return toProblem(throwable, request, GeneralErrorKey.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  default R toProblem(final Throwable throwable, final T request,
                      final String defaultErrorKey, HttpStatus defaultStatus) {

    String errorKey = ClassUtils.getName(throwable);

    ProblemMessageSourceResolver statusResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.STATUS_CODE_PREFIX + errorKey, defaultStatus.value());
    HttpStatus status = defaultStatus;
    try {
      String statusCode = ProblemMessageProvider.getMessage(statusResolver);
      status = HttpStatus.valueOf(statusCode);
    } catch (final Exception e) {
      // Ignore on purpose
    }

    ProblemMessageSourceResolver defaultCodeResolver = ProblemMessageSourceResolver.of(ProblemConstant.CODE_CODE_PREFIX + defaultErrorKey,
        status.value());
    ProblemMessageSourceResolver defaultTitleResolver = ProblemMessageSourceResolver.of(ProblemConstant.TITLE_CODE_PREFIX + defaultErrorKey,
        status.getReasonPhrase());
    ProblemMessageSourceResolver defaultDetailResolver = ProblemMessageSourceResolver.of(ProblemConstant.DETAIL_CODE_PREFIX + defaultErrorKey,
        throwable.getMessage());

    ProblemMessageSourceResolver codeResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.CODE_CODE_PREFIX + errorKey, ProblemMessageProvider.getMessage(defaultCodeResolver));
    ProblemMessageSourceResolver titleResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.TITLE_CODE_PREFIX + errorKey, ProblemMessageProvider.getMessage(defaultTitleResolver));
    ProblemMessageSourceResolver detailResolver = ProblemMessageSourceResolver
        .of(ProblemConstant.DETAIL_CODE_PREFIX + errorKey, ProblemMessageProvider.getMessage(defaultDetailResolver));

    Problem problem = toProblem(throwable, codeResolver, titleResolver,
        detailResolver, statusResolver);
    return create(throwable, request, status, problem);
  }

  default void log(final Throwable throwable, final HttpStatus status) {
    logger.error(status.getReasonPhrase(), throwable);
//		throwable.printStackTrace();
  }

//	default void log(final Throwable throwable, final HttpStatus status) {
//		if (status.is4xxClientError()) {
//			logger.warn("{}: {}", status.getReasonPhrase(), throwable.getMessage());
//		} else if (status.is5xxServerError()) {
//			logger.error(status.getReasonPhrase(), throwable);
//		}
//	}

  // ErrorResponseBuilder can have different implementations as per consumer needs, So can be overridden.
  // Would be different for Web or Webflux applications
  default ErrorResponseBuilder<T, R> errorResponseBuilder() {
    return ProblemBeanRegistry.errorResponseBuilder();
  }
}
