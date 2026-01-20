package com.ksoot.problem.spring.advice.network;

import com.ksoot.problem.spring.advice.AdviceTrait;
import net.jodah.failsafe.CircuitBreakerOpenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link CircuitBreakerOpenException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see CircuitBreakerOpenException
 * @see HttpStatus#SERVICE_UNAVAILABLE
 */
public interface CircuitBreakerOpenAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link CircuitBreakerOpenException} and converts it into a response. Adds {@code
   * Retry-After} header to the response.
   *
   * @param exception the circuit breaker open exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleCircuitBreakerOpen(final CircuitBreakerOpenException exception, final T request) {
    final long delay = exception.getCircuitBreaker().getRemainingDelay().getSeconds();
    final HttpHeaders headers = retryAfter(delay);
    return toResponse(exception, request, HttpStatus.SERVICE_UNAVAILABLE, headers);
  }

  /**
   * Creates {@link HttpHeaders} with the {@code Retry-After} header.
   *
   * @param delay the delay in seconds
   * @return the headers
   */
  default HttpHeaders retryAfter(final long delay) {
    final HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.RETRY_AFTER, String.valueOf(delay));
    return headers;
  }
}
