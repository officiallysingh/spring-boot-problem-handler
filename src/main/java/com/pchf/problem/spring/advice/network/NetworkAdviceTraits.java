package com.pchf.problem.spring.advice.network;

import com.pchf.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface NetworkAdviceTraits<T, R> extends
    CircuitBreakerOpenAdviceTrait<T, R> {
}
