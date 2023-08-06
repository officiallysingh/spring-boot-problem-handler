package com.ksoot.problem.spring.advice.application;

import com.ksoot.problem.spring.advice.BaseAdviceTrait;

/**
 * @see BaseAdviceTrait
 */
public interface ApplicationAdviceTraits<T, R> extends
    ApplicationProblemAdviceTrait<T, R>,
    ApplicationExceptionAdviceTrait<T, R>,
    ApplicationMultiProblemAdviceTrait<T, R> {

}
