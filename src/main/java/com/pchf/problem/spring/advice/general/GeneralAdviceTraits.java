package com.pchf.problem.spring.advice.general;

import com.pchf.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface GeneralAdviceTraits<T, R> extends
    ProblemAdviceTrait<T, R>,
    ThrowableAdviceTrait<T, R>,
    UnsupportedOperationAdviceTrait<T, R> {
}
