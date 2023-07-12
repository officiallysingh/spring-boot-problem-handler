package com.pchf.problem.spring.advice.network;

import com.pchf.problem.spring.advice.AdviceTrait;
import net.jodah.failsafe.CircuitBreakerOpenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface CircuitBreakerOpenAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleCircuitBreakerOpen(final CircuitBreakerOpenException exception, final T request) {
    final long delay = exception.getCircuitBreaker().getRemainingDelay().getSeconds();
    final HttpHeaders headers = retryAfter(delay);
    return create(exception, request, HttpStatus.SERVICE_UNAVAILABLE, headers);
  }

  default HttpHeaders retryAfter(final long delay) {
    final HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.RETRY_AFTER, String.valueOf(delay));
    return headers;
  }
}
