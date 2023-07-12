package com.pchf.problem.spring.advice.dao;

import org.springframework.core.Ordered;

/**
 * @author Rajveer Singh
 */
public interface ConstraintNameResolver extends Ordered {

  String resolveConstraintName(final String exceptionMessage);

  DBType dbType();
  // TODO: Provide implementation for the target database
  // DB2, DERBY, H2, HANA, HSQL, INFORMIX, MYSQL, ORACLE, POSTGRESQL, SQL_SERVER, SYBASE

  default int getOrder() {
    return 0;
  }
}
