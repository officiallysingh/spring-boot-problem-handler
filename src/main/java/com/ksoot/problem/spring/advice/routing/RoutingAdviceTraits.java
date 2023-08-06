package com.ksoot.problem.spring.advice.routing;

import com.ksoot.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface RoutingAdviceTraits<T, R> extends
    MissingServletRequestParameterAdviceTrait<T, R>,
    MissingServletRequestPartAdviceTrait<T, R>,
    MissingRequestHeaderAdviceTrait<T, R>,
    NoHandlerFoundAdviceTrait<T, R>,
    ServletRequestBindingAdviceTrait<T, R> {
}
