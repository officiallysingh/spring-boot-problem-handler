package com.pchf.problem.spring.boot.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Condition to register Jackson Problem Module.
 */
public class ProblemJacksonEnabled extends AllNestedConditions {

  public ProblemJacksonEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @ConditionalOnClass(Module.class)
  static class JacksonAvailable {
  }


  @ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
  static class ProblemEnabled {
  }

  @ConditionalOnProperty(prefix = "problem", name = "jackson-module-enabled", havingValue = "true", matchIfMissing = true)
  static class JacksonModuleEnabled {
  }
}
