package com.pchf.problem.spring.advice.http;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MethodNotAllowedException;

import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public interface MethodNotAllowedAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMethodNotAllowedException(final MethodNotAllowedException exception, final T request) {
    @Nullable final Set<HttpMethod> methods = exception.getSupportedMethods();
    String requestedMethod = exception.getHttpMethod();
    String allowedMethods = CollectionUtils.isEmpty(methods) ? "None"
        : methods.stream().map(HttpMethod::name).collect(Collectors.joining(","));
    Problem problem = toProblem(exception, HttpStatus.METHOD_NOT_ALLOWED,
        ProblemMessageSourceResolver.of(ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.METHOD_NOT_ALLOWED,
            "Requested Method: {0} not allowed, allowed methods are: {1}", new Object[]{requestedMethod, allowedMethods}));

    final HttpHeaders headers = new HttpHeaders();
    headers.setAllow(requireNonNull(methods));
    return create(exception, request, HttpStatus.METHOD_NOT_ALLOWED, headers, problem);
  }
}
