package com.pchf.problem.spring.advice.dao;

/**
 * @author Rajveer Singh
 */
public class SQLServerConstraintNameResolver implements ConstraintNameResolver {

  /*
   * (non-Javadoc)
   *
   * @see com.ksoot.framework.common.spring.config.error.DBConstraintNameResolver#
   * resolveConstraintName(org.springframework.dao.
   * DataIntegrityViolationException)
   */
  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    try {
      return exceptionMessage.substring(exceptionMessage.indexOf("\"") + 1, exceptionMessage.lastIndexOf("\""));
    } catch (final Exception e) {
      // Ignored on purpose
    }
    return "sqlserver.duplicate.key";
  }

  public DBType dbType() {
    return DBType.SQL_SERVER;
  }
}
