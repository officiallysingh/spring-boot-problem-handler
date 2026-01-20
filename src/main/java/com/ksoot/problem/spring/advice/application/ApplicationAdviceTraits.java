package com.ksoot.problem.spring.advice.application;

/**
 * Composite advice trait for application-specific exceptions and problems.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see ApplicationProblemAdviceTrait
 * @see ApplicationExceptionAdviceTrait
 * @see ApplicationMultiProblemAdviceTrait
 */
public interface ApplicationAdviceTraits<T, R>
    extends ApplicationProblemAdviceTrait<T, R>,
        ApplicationExceptionAdviceTrait<T, R>,
        ApplicationMultiProblemAdviceTrait<T, R> {}
