package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/** Condition to check if an ORM (RDBMS) URL is available in configuration. */
public class ORMUrlAvailable extends AnyNestedCondition {

  /** Constructs a new {@code ORMUrlAvailable} condition. */
  public ORMUrlAvailable() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for Spring Datasource URL availability. */
  @ConditionalOnProperty(prefix = "spring.datasource", name = "url")
  static class SpringDatasourceUrlAvailable {}

  /** Condition for Spring R2DBC URL availability. */
  @ConditionalOnProperty(prefix = "spring.r2dbc", name = "url")
  static class SpringR2dbcUrlAvailable {}
}
