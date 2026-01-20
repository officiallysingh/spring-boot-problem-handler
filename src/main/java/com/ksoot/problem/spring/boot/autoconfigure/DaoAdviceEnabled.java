package com.ksoot.problem.spring.boot.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.MongoDatabaseFactory;

/** Condition to register advice for DAO exception handling. */
public class DaoAdviceEnabled extends AnyNestedCondition {

  /** Constructs a new {@code DaoAdviceEnabled} condition. */
  DaoAdviceEnabled() {
    super(ConfigurationPhase.PARSE_CONFIGURATION);
  }

  /** Condition for JPA/ORM availability. */
  @Conditional(ORMUrlAvailable.class)
  @ConditionalOnClass(value = {JpaRepository.class})
  static class ORMAvailable {}

  /** Condition for MongoDB availability. */
  @ConditionalOnClass(value = {MongoDatabaseFactory.class})
  @ConditionalOnProperty(prefix = "spring.mongodb", name = "uri")
  static class MongoAvailable {}
}
