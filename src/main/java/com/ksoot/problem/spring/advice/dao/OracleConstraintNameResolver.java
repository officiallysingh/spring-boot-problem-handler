package com.ksoot.problem.spring.advice.dao;

/**
 * {@link ConstraintNameResolver} implementation for Oracle.
 *
 * @author Rajveer Singh
 */
public class OracleConstraintNameResolver implements ConstraintNameResolver {

  /** {@inheritDoc} */
  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Auto-generated method stub");
  }

  /** {@inheritDoc} */
  @Override
  public DBType dbType() {
    return DBType.ORACLE;
  }
}
