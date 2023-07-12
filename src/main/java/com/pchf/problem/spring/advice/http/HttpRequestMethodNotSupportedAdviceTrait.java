package com.pchf.problem.spring.advice.http;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.util.Objects.requireNonNull;

public interface HttpRequestMethodNotSupportedAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception, final T request) {
    @Nullable final String[] methods = exception.getSupportedMethods();
    String requestedMethod = exception.getMethod();
    String allowedMethods = ArrayUtils.isEmpty(methods) ? "None" : String.join(",", methods);
    Problem problem = toProblem(exception, HttpStatus.METHOD_NOT_ALLOWED,
        ProblemMessageSourceResolver.of(ProblemConstant.MESSAGE_CODE_PREFIX + GeneralErrorKey.REQUEST_METHOD_NOT_SUPPORTED,
            "Requested Method: {0} not allowed, allowed methods are: {1}", new Object[]{requestedMethod, allowedMethods}),
        ProblemMessageSourceResolver.of(ProblemConstant.DETAILS_CODE_PREFIX + GeneralErrorKey.REQUEST_METHOD_NOT_SUPPORTED,
            exception.getMessage()));

    final HttpHeaders headers = new HttpHeaders();
    headers.setAllow(requireNonNull(exception.getSupportedHttpMethods()));
    return create(exception, request, HttpStatus.METHOD_NOT_ALLOWED, headers, problem);
  }
}