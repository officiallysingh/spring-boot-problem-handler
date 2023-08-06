package com.ksoot.problem.spring.advice.general;

import com.ksoot.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface GeneralAdviceTraits<T, R> extends
    ProblemAdviceTrait<T, R>,
    ThrowableAdviceTrait<T, R>,
    UnsupportedOperationAdviceTrait<T, R> {
}
