package com.ksoot.problem.spring.advice.dao;

public interface DaoAdviceTraits<T, R>
    extends DataIntegrityViolationAdviceTrait<T, R>, DuplicateKeyExceptionAdviceTrait<T, R> {}
