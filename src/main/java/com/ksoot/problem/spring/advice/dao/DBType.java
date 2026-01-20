package com.ksoot.problem.spring.advice.dao;

/**
 * Enum representing supported database types for constraint name resolution.
 *
 * @see ConstraintNameResolver
 */
public enum DBType {
  /** Microsoft SQL Server. */
  SQL_SERVER,
  /** PostgreSQL. */
  POSTGRESQL,
  /** MySQL. */
  MYSQL,
  /** Oracle Database. */
  ORACLE,
  /** MongoDB. */
  MONGO_DB;
}
