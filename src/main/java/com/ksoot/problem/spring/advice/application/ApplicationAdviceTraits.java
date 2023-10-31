package com.ksoot.problem.spring.advice.application;

public interface ApplicationAdviceTraits<T, R>
    extends ApplicationProblemAdviceTrait<T, R>,
        ApplicationExceptionAdviceTrait<T, R>,
        ApplicationMultiProblemAdviceTrait<T, R> {}
