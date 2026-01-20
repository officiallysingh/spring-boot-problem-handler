package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/** Condition to register advice for Security exception handling depending on configurations. */
public class SecurityAdviceEnabled extends AllNestedConditions {

  /** Constructs a new {@code SecurityAdviceEnabled} condition. */
  public SecurityAdviceEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for problem handling being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ProblemEnabled {}

  /** Condition for security advice being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "security-advice-enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class SecurityEnabled {}
}
