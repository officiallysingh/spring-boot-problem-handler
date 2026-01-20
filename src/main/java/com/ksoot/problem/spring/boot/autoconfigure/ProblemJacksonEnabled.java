package com.ksoot.problem.spring.boot.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/** Condition to register Jackson Problem Module. */
public class ProblemJacksonEnabled extends AllNestedConditions {

  /** Constructs a new {@code ProblemJacksonEnabled} condition. */
  public ProblemJacksonEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for Jackson availability. */
  @ConditionalOnClass(Module.class)
  static class JacksonAvailable {}

  /** Condition for problem handling being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ProblemEnabled {}

  /** Condition for Jackson Problem module being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "jackson-module-enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class JacksonModuleEnabled {}
}
