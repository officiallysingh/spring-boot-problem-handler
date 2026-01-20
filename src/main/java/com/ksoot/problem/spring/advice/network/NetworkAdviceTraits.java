package com.ksoot.problem.spring.advice.network;

/**
 * Composite advice trait that combines all network-related advice traits.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see CircuitBreakerOpenAdviceTrait
 */
public interface NetworkAdviceTraits<T, R> extends CircuitBreakerOpenAdviceTrait<T, R> {}
