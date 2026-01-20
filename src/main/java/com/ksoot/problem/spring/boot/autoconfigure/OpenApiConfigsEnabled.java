package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/** Condition to register advice for OpenAPI exception handling, depending on configurations. */
public class OpenApiConfigsEnabled extends AnyNestedCondition {

  /** Constructs a new {@code OpenApiConfigsEnabled} condition. */
  public OpenApiConfigsEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for OpenAPI request validation being enabled. */
  @ConditionalOnProperty(
      prefix = "problem.open-api",
      name = "req-validation-enabled",
      havingValue = "true")
  static class ReqValidationEnabled {}

  /** Condition for OpenAPI response validation being enabled. */
  @ConditionalOnProperty(
      prefix = "problem.open-api",
      name = "res-validation-enabled",
      havingValue = "true")
  static class ResValidationEnabled {}
}
