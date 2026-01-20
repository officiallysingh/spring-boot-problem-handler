package com.ksoot.problem.spring.advice.dao;

/**
 * {@link ConstraintNameResolver} implementation for PostgreSQL.
 *
 * @author Rajveer Singh
 */
public class PostgresConstraintNameResolver implements ConstraintNameResolver {

  /** {@inheritDoc} */
  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    String exMessage = exceptionMessage.trim();
    try {
      exMessage = exMessage.substring(exMessage.indexOf("constraint") + 12);
      return exMessage.substring(0, exMessage.indexOf("\""));
    } catch (final Exception e) {
      // Ignored on purpose
    }
    return "postgres.duplicate.key";
  }

  /** {@inheritDoc} */
  @Override
  public DBType dbType() {
    return DBType.POSTGRESQL;
  }
}
