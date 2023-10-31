package com.ksoot.problem.spring.advice.http;

public interface HttpAdviceTraits<T, R>
    extends HttpMediaTypeNotAcceptableAdviceTrait<T, R>,
        HttpMediaTypeNotSupportedExceptionAdviceTrait<T, R>,
        UnsupportedMediaTypeStatusAdviceTrait<T, R>,
        HttpRequestMethodNotSupportedAdviceTrait<T, R>,
        MethodNotAllowedAdviceTrait<T, R>,
        NotAcceptableStatusAdviceTrait<T, R>,
        ResponseStatusAdviceTrait<T, R> {}
