package com.ksoot.problem.spring.advice.general;

/**
 * Composite advice trait that combines all general-purpose advice traits.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see ProblemAdviceTrait
 * @see ThrowableAdviceTrait
 * @see UnsupportedOperationAdviceTrait
 */
public interface GeneralAdviceTraits<T, R>
    extends ProblemAdviceTrait<T, R>,
        ThrowableAdviceTrait<T, R>,
        UnsupportedOperationAdviceTrait<T, R> {}
