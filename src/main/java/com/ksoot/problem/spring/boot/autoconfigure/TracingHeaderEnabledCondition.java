package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/** Condition to check if a tracing is enabled and configured to use header as trace id. */
public class TracingHeaderEnabledCondition extends AllNestedConditions {

  /** Constructs a new {@code TracingHeaderEnabledCondition} condition. */
  public TracingHeaderEnabledCondition() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for problem handling being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ProblemEnabled {}

  /** Condition for tracing being enabled. */
  @ConditionalOnProperty(prefix = "problem.tracing", name = "enabled", havingValue = "true")
  static class TracingEnabled {}

  /** Condition for tracing strategy being set to {@code HEADER}. */
  @ConditionalOnProperty(prefix = "problem.tracing", name = "strategy", havingValue = "HEADER")
  static class TraceIdHeaderStrategy {}
}
