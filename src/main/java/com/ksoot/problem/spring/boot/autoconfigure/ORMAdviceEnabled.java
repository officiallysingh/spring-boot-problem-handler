package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Condition to register advice for ORM exception handling.
 */
public class ORMAdviceEnabled extends AllNestedConditions {

	public ORMAdviceEnabled() {
		super(ConfigurationPhase.PARSE_CONFIGURATION);
	}

	@ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
	static class ProblemEnabled {
	}

	@ConditionalOnProperty(prefix = "problem", name = "dao-advice-enabled", havingValue = "true", matchIfMissing = true)
	static class ORMEnabledEnabled {
	}
}
