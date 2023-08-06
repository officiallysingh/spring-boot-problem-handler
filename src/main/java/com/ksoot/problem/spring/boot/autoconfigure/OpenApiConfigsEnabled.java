package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Condition to register advice for OpenAPI exception handling.
 */
public class OpenApiConfigsEnabled extends AnyNestedCondition {

  public OpenApiConfigsEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @ConditionalOnProperty(prefix = "problem.open-api", name = "req-validation-enabled", havingValue = "true")
  static class ReqValidationEnabled {
  }

  @ConditionalOnProperty(prefix = "problem.open-api", name = "res-validation-enabled", havingValue = "true")
  static class ResValidationEnabled {
  }

}
