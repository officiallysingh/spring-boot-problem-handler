package com.ksoot.problem.spring.advice.dao;

/**
 * Enum representing supported databases in Spring Boot applications. Typically maps to the
 * "spring.jpa.database" property.
 */
public enum Database {
  /** Default database. */
  DEFAULT,
  /** IBM DB2. */
  DB2,
  /** Apache Derby. */
  DERBY,
  /** H2 Database. */
  H2,
  /** SAP HANA. */
  HANA,
  /** HSQLDB. */
  HSQL,
  /** IBM Informix. */
  INFORMIX,
  /** MySQL. */
  MYSQL,
  /** Oracle Database. */
  ORACLE,
  /** PostgreSQL. */
  POSTGRESQL,
  /** Microsoft SQL Server. */
  SQL_SERVER,
  /** Sybase. */
  SYBASE
}
