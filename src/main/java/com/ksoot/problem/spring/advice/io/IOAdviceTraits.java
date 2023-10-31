package com.ksoot.problem.spring.advice.io;

public interface IOAdviceTraits<T, R>
    extends MessageNotReadableAdviceTrait<T, R>,
        MaxUploadSizeExceededExceptionAdviceTrait<T, R>,
        DataBufferLimitExceptionAdviceTrait<T, R>,
        MultipartAdviceTrait<T, R> {}
