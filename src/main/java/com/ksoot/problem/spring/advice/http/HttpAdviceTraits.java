package com.ksoot.problem.spring.advice.http;

/**
 * Composite advice trait that combines all HTTP-related advice traits.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see HttpMediaTypeNotAcceptableAdviceTrait
 * @see HttpMediaTypeNotSupportedExceptionAdviceTrait
 * @see UnsupportedMediaTypeStatusAdviceTrait
 * @see HttpRequestMethodNotSupportedAdviceTrait
 * @see MethodNotAllowedAdviceTrait
 * @see NotAcceptableStatusAdviceTrait
 * @see ResponseStatusAdviceTrait
 */
public interface HttpAdviceTraits<T, R>
    extends HttpMediaTypeNotAcceptableAdviceTrait<T, R>,
        HttpMediaTypeNotSupportedExceptionAdviceTrait<T, R>,
        UnsupportedMediaTypeStatusAdviceTrait<T, R>,
        HttpRequestMethodNotSupportedAdviceTrait<T, R>,
        MethodNotAllowedAdviceTrait<T, R>,
        NotAcceptableStatusAdviceTrait<T, R>,
        ResponseStatusAdviceTrait<T, R> {}
