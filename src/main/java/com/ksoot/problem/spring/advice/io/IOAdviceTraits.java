package com.ksoot.problem.spring.advice.io;

import com.ksoot.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface IOAdviceTraits<T, R> extends MessageNotReadableAdviceTrait<T, R>,
		MaxUploadSizeExceededExceptionAdviceTrait<T, R>, MultipartAdviceTrait<T, R> {
}
