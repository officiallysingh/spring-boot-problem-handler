package com.ksoot.problem.spring.advice.routing;

public interface RoutingAdviceTraits<T, R>
    extends MissingServletRequestParameterAdviceTrait<T, R>,
        MissingServletRequestPartAdviceTrait<T, R>,
        MissingRequestHeaderAdviceTrait<T, R>,
        NoHandlerFoundAdviceTrait<T, R>,
        ServletRequestBindingAdviceTrait<T, R> {}
