package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.mongodb.MongoDatabaseFactory;

/**
 * Condition to register advice for DAO exception handling.
 */
public class DaoAdviceEnabled extends AnyNestedCondition {

  DaoAdviceEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  @Conditional(ORMUrlAvailable.class)
  static class ORMAvailable {

  }

  @ConditionalOnClass(value = {MongoDatabaseFactory.class})
  @ConditionalOnProperty(prefix = "spring.data.mongodb", name = "uri")
  static class MongoAvailable {

  }
}
