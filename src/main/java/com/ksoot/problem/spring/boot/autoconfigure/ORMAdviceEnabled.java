package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/** Condition to register advice for ORM exception handling depending on configurations. */
public class ORMAdviceEnabled extends AllNestedConditions {

  /** Constructs a new {@code ORMAdviceEnabled} condition. */
  public ORMAdviceEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for problem handling being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ProblemEnabled {}

  /** Condition for DAO advice being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "dao-advice-enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ORMEnabled {}
}
