package com.pchf.problem.spring.advice.io;

import com.pchf.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface IOAdviceTraits<T, R> extends
    MessageNotReadableAdviceTrait<T, R>,
    MaxUploadSizeExceededExceptionAdviceTrait<T, R>,
    MultipartAdviceTrait<T, R> {
}
