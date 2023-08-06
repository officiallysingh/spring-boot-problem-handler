package com.ksoot.problem.spring.advice.http;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
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
        ProblemMessageSourceResolver.of(ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.REQUEST_METHOD_NOT_SUPPORTED,
            "Requested Method: {0} not allowed, allowed methods are: {1}", new Object[]{requestedMethod, allowedMethods}));

    final HttpHeaders headers = new HttpHeaders();
    headers.setAllow(requireNonNull(exception.getSupportedHttpMethods()));
    return create(exception, request, HttpStatus.METHOD_NOT_ALLOWED, headers, problem);
  }
}
