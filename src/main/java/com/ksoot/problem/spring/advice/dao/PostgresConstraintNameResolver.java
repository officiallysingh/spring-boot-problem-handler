package com.ksoot.problem.spring.advice.dao;

/**
 * @author Rajveer Singh
 */
public class PostgresConstraintNameResolver implements ConstraintNameResolver {

	/*
	 * (non-Javadoc)
	 *
	 * @see com.ksoot.framework.common.spring.config.error.dbconstraint.
	 * ConstraintNameResolver# resolveConstraintName(org.springframework.dao.
	 * DataIntegrityViolationException)
	 */
	@Override
	public String resolveConstraintName(final String exceptionMessage) {
		String exMessage = exceptionMessage.trim();
		try {
			exMessage = exMessage.substring(exMessage.indexOf("constraint") + 12);
			return exMessage.substring(0, exMessage.indexOf("\""));
		}
		catch (final Exception e) {
			// Ignored on purpose
		}
		return "postgres.duplicate.key";
	}

	public DBType dbType() {
		return DBType.POSTGRESQL;
	}
}
