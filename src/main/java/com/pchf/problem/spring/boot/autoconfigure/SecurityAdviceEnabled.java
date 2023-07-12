package com.pchf.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Condition to register advice for Security exception handling.
 */
public class SecurityAdviceEnabled extends AllNestedConditions {

  public SecurityAdviceEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
  static class ProblemEnabled {
  }

  @ConditionalOnProperty(prefix = "problem", name = "security-advice-enabled", havingValue = "true", matchIfMissing = true)
  static class SecurityEnabled {
  }
}
