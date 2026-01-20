package com.ksoot.problem.spring.advice.dao;

/**
 * {@link ConstraintNameResolver} implementation for SQL Server.
 *
 * @author Rajveer Singh
 */
public class SQLServerConstraintNameResolver implements ConstraintNameResolver {

  /** {@inheritDoc} */
  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    try {
      return exceptionMessage.substring(
          exceptionMessage.indexOf("\"") + 1, exceptionMessage.lastIndexOf("\""));
    } catch (final Exception e) {
      // Ignored on purpose
    }
    return "sqlserver.duplicate.key";
  }

  /** {@inheritDoc} */
  @Override
  public DBType dbType() {
    return DBType.SQL_SERVER;
  }
}
