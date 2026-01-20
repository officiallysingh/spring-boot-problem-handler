package com.ksoot.problem.spring.advice.http;

import static java.util.Objects.requireNonNull;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import jakarta.annotation.Nullable;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

/**
 * Advice trait to handle {@link MethodNotAllowedException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see MethodNotAllowedException
 * @see HttpStatus#METHOD_NOT_ALLOWED
 */
public interface MethodNotAllowedAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link MethodNotAllowedException} and converts it into a response.
   *
   * @param exception the exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleMethodNotAllowedException(
      final MethodNotAllowedException exception, final T request) {
    @Nullable final Set<HttpMethod> methods = exception.getSupportedMethods();
    String requestedMethod = exception.getHttpMethod();
    String allowedMethods =
        CollectionUtils.isEmpty(methods)
            ? "None"
            : methods.stream().map(HttpMethod::name).collect(Collectors.joining(","));
    Problem problem =
        toProblem(
            exception,
            HttpStatus.METHOD_NOT_ALLOWED,
            ProblemMessageSourceResolver.of(
                ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.METHOD_NOT_ALLOWED,
                "Requested Method: {0} not allowed, allowed methods are: {1}",
                new Object[] {requestedMethod, allowedMethods}));

    final HttpHeaders headers = new HttpHeaders();
    headers.setAllow(requireNonNull(methods));
    return buildResponse(exception, request, HttpStatus.METHOD_NOT_ALLOWED, headers, problem);
  }
}
