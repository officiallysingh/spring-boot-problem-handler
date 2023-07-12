package com.pchf.problem.spring.advice.application;

import com.pchf.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface ApplicationAdviceTraits<T, R> extends
    ApplicationProblemAdviceTrait<T, R>,
    ApplicationExceptionAdviceTrait<T, R>,
    ApplicationMultiProblemAdviceTrait<T, R> {

}
