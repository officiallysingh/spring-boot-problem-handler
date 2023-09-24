package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Condition to register advice for Dao exception handling.
 */
public class ORMUrlAvailable extends AnyNestedCondition {

	public ORMUrlAvailable() {
		super(ConfigurationPhase.PARSE_CONFIGURATION);
	}

	@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
	static class SpringDatasourceUrlAvailable {
	}

	@ConditionalOnProperty(prefix = "spring.r2dbc", name = "url")
	static class SpringR2dbcUrlAvailable {
	}
}
