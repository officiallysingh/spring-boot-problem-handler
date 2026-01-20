package com.ksoot.problem.spring.advice.dao;

/**
 * {@link ConstraintNameResolver} implementation for MySQL.
 *
 * @author Rajveer Singh
 */
public class MysqlConstraintNameResolver implements ConstraintNameResolver {

  /** {@inheritDoc} */
  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Auto-generated method stub");
  }

  /** {@inheritDoc} */
  @Override
  public DBType dbType() {
    return DBType.MYSQL;
  }
}
