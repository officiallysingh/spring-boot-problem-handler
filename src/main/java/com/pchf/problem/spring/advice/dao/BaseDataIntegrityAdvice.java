package com.pchf.problem.spring.advice.dao;

import com.pchf.problem.spring.advice.AdviceTrait;

public interface BaseDataIntegrityAdvice<T, R> extends AdviceTrait<T, R> {

  String resolveConstraintName(final String exceptionMessage);
}
