package com.pchf.problem.spring.advice.security;

public interface SecurityAdviceTraits<T, R> extends
    AuthenticationAdviceTrait<T, R>,
    InsufficientAuthenticationAdviceTrait<T, R>,
    AccessDeniedAdviceTrait<T, R> {

}
