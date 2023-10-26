package com.ksoot.problem.spring.advice.dao;

import com.ksoot.problem.spring.advice.AdviceTrait;

public interface BaseDataIntegrityAdvice<T, R> extends AdviceTrait<T, R> {

  String resolveConstraintName(final String exceptionMessage);
}
