package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class TracingHeaderEnabledCondition extends AllNestedConditions {

  public TracingHeaderEnabledCondition() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @ConditionalOnProperty(
      prefix = "problem",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ProblemEnabled {}

  @ConditionalOnProperty(prefix = "problem.tracing", name = "enabled", havingValue = "true")
  static class TracingEnabled {}

  @ConditionalOnProperty(prefix = "problem.tracing", name = "strategy", havingValue = "HEADER")
  static class TraceIdHeaderStrategy {}
}
