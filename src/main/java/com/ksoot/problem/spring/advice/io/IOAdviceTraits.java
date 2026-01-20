package com.ksoot.problem.spring.advice.io;

/**
 * Composite advice trait for Input/Output (I/O) related exceptions.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see MessageNotReadableAdviceTrait
 * @see MaxUploadSizeExceededExceptionAdviceTrait
 * @see DataBufferLimitExceptionAdviceTrait
 * @see MultipartAdviceTrait
 */
public interface IOAdviceTraits<T, R>
    extends MessageNotReadableAdviceTrait<T, R>,
        MaxUploadSizeExceededExceptionAdviceTrait<T, R>,
        DataBufferLimitExceptionAdviceTrait<T, R>,
        MultipartAdviceTrait<T, R> {}
