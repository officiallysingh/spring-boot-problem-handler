package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;

/** Condition to register advice for OpenAPI exception handling. */
public class OpenAPIValidationAdviceEnabled extends AllNestedConditions {

  public OpenAPIValidationAdviceEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @ConditionalOnProperty(
      prefix = "problem",
      name = "enabled",
      havingValue = "true",
      matchIfMissing = true)
  static class ProblemEnabled {}

  @ConditionalOnProperty(prefix = "problem.open-api", name = "path")
  static class OpenAPISpecAvailable {}

  @Conditional(OpenApiConfigsEnabled.class)
  static class OpenApiPropertiesEnabled {}
}
