package com.pchf.problem.spring.advice.dao;

/**
 * @author Rajveer Singh
 */
public class OracleConstraintNameResolver implements ConstraintNameResolver {

  /*
   * (non-Javadoc)
   *
   * @see
   * com.ksoot.framework.common.spring.config.error.db.ConstraintNameResolver#
   * resolveConstraintName(org.springframework.dao.
   * DataIntegrityViolationException)
   */
  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Auto-generated method stub");
  }

  public DBType dbType() {
    return DBType.ORACLE;
  }
}
