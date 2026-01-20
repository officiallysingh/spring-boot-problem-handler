package com.ksoot.problem.spring.advice.dao;

import org.springframework.core.Ordered;

/**
 * Interface for resolving database constraint names from exception messages. Implementations should
 * provide database-specific logic to extract constraint names.
 *
 * @author Rajveer Singh
 * @see DBType
 */
public interface ConstraintNameResolver extends Ordered {

  /**
   * Resolves the constraint name from the given exception message.
   *
   * @param exceptionMessage the database exception message
   * @return the extracted constraint name, or a default value if not found
   */
  String resolveConstraintName(final String exceptionMessage);

  /**
   * Returns the database type supported by this resolver.
   *
   * @return the database type
   */
  DBType dbType();

  // TODO: Provide implementation for the target database
  // DB2, DERBY, H2, HANA, HSQL, INFORMIX, MYSQL, ORACLE, POSTGRESQL, SQL_SERVER, SYBASE

  /** {@inheritDoc} */
  @Override
  default int getOrder() {
    return 0;
  }
}
