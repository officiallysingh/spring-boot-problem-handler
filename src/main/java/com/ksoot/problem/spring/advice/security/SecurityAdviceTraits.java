package com.ksoot.problem.spring.advice.security;

/**
 * Composite advice trait that combines all security-related advice traits.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see AuthenticationExceptionAdviceTrait
 * @see InsufficientAuthenticationAdviceTrait
 * @see AccessDeniedExceptionAdviceTrait
 */
public interface SecurityAdviceTraits<T, R>
    extends UsernameNotFoundAdviceTrait<T, R>,
        BadCredentialsExceptionAdviceTrait<T, R>,
        AuthenticationExceptionAdviceTrait<T, R>,
        InsufficientAuthenticationAdviceTrait<T, R>,
        AccessDeniedExceptionAdviceTrait<T, R> {}
