package com.ksoot.problem.spring.advice.dao;

import com.ksoot.problem.spring.advice.AdviceTrait;

/**
 * Base advice trait for handling data integrity-related exceptions.
 *
 * @param <T> the request type
 * @param <R> the response type
 */
public interface BaseDataIntegrityAdvice<T, R> extends AdviceTrait<T, R> {

  /**
   * Resolves the constraint name from the exception message.
   *
   * @param exceptionMessage the exception message
   * @return the resolved constraint name
   */
  String resolveConstraintName(final String exceptionMessage);
}
