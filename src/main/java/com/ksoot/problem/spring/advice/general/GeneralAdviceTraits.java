package com.ksoot.problem.spring.advice.general;

public interface GeneralAdviceTraits<T, R>
    extends ProblemAdviceTrait<T, R>,
        ThrowableAdviceTrait<T, R>,
        UnsupportedOperationAdviceTrait<T, R> {}
