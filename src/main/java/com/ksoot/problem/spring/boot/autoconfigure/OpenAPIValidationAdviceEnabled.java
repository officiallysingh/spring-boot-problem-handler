package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

/** Condition to register advice for OpenAPI exception handling. */
public class OpenAPIValidationAdviceEnabled extends AllNestedConditions {

  /** Constructs a new {@code OpenAPIValidationAdviceEnabled} condition. */
  public OpenAPIValidationAdviceEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for problem handling being enabled. */
  @ConditionalOnProperty(
      prefix = "problem",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ProblemEnabled {}

  /** Condition for OpenAPI specification path availability. */
  @ConditionalOnProperty(prefix = "problem.open-api", name = "path")
  static class OpenAPISpecAvailable {}

  /** Condition for OpenAPI configurations being enabled. */
  @Conditional(OpenApiConfigsEnabled.class)
  static class OpenApiPropertiesEnabled {}
}
