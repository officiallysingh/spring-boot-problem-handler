package com.ksoot.problem.spring.advice.routing;

/**
 * Composite advice trait that combines all routing-related advice traits.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see MissingServletRequestParameterAdviceTrait
 * @see MissingServletRequestPartAdviceTrait
 * @see MissingRequestHeaderAdviceTrait
 * @see NoHandlerFoundAdviceTrait
 * @see ServletRequestBindingAdviceTrait
 */
public interface RoutingAdviceTraits<T, R>
    extends MissingServletRequestParameterAdviceTrait<T, R>,
        MissingServletRequestPartAdviceTrait<T, R>,
        MissingRequestHeaderAdviceTrait<T, R>,
        NoHandlerFoundAdviceTrait<T, R>,
        ServletRequestBindingAdviceTrait<T, R> {}
