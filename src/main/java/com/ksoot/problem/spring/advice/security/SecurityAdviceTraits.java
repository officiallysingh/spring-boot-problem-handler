package com.ksoot.problem.spring.advice.security;

/**
 * Composite advice trait that combines all security-related advice traits.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see AuthenticationAdviceTrait
 * @see InsufficientAuthenticationAdviceTrait
 * @see AccessDeniedAdviceTrait
 */
public interface SecurityAdviceTraits<T, R>
    extends AuthenticationAdviceTrait<T, R>,
        InsufficientAuthenticationAdviceTrait<T, R>,
        AccessDeniedAdviceTrait<T, R> {}
