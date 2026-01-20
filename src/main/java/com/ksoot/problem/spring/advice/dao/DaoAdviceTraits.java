package com.ksoot.problem.spring.advice.dao;

/**
 * Composite advice trait for Data Access Object (DAO) exceptions.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see DataIntegrityViolationAdviceTrait
 * @see DuplicateKeyExceptionAdviceTrait
 */
public interface DaoAdviceTraits<T, R>
    extends DataIntegrityViolationAdviceTrait<T, R>, DuplicateKeyExceptionAdviceTrait<T, R> {}
