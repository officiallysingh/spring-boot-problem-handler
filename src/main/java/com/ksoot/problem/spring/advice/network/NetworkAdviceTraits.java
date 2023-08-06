package com.ksoot.problem.spring.advice.network;

import com.ksoot.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface NetworkAdviceTraits<T, R> extends
    CircuitBreakerOpenAdviceTrait<T, R> {
}
