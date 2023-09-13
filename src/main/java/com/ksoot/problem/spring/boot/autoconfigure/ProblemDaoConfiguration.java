package com.ksoot.problem.spring.boot.autoconfigure;

import com.ksoot.problem.spring.advice.dao.ConstraintNameResolver;
import com.ksoot.problem.spring.advice.dao.MongoConstraintNameResolver;
import com.ksoot.problem.spring.advice.dao.PostgresConstraintNameResolver;
import com.ksoot.problem.spring.advice.dao.SQLServerConstraintNameResolver;
import com.ksoot.problem.spring.config.ProblemProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(value = {DaoAdviceEnabled.class, ORMAdviceEnabled.class})
@ConditionalOnWebApplication
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class ProblemDaoConfiguration {

  @ConditionalOnMissingBean(name = "postgresqlConstraintNameResolver")
  @Conditional(ORMUrlAvailable.class)
  @ConditionalOnProperty(prefix = "spring.jpa", name = "database", havingValue = "POSTGRESQL")
  public static class PostgresqlConstraintNameResolverConfiguration {

    @Bean
    ConstraintNameResolver postgresqlConstraintNameResolver(final Environment env) {
      return new PostgresConstraintNameResolver();
    }
  }

  @ConditionalOnMissingBean(name = "sqlServerConstraintNameResolver")
  @Conditional(ORMUrlAvailable.class)
  @ConditionalOnProperty(prefix = "spring.jpa", name = "database", havingValue = "SQL_SERVER")
  public static class SQLServerConstraintNameResolverConfiguration {

    @Bean
    ConstraintNameResolver sqlServerConstraintNameResolver(final Environment env) {
      return new SQLServerConstraintNameResolver();
    }
  }

  @ConditionalOnClass(value = {MongoDatabaseFactory.class})
  @ConditionalOnProperty(prefix = "spring.data.mongodb", name = "uri")
  @ConditionalOnMissingBean(name = "mongoConstraintNameResolver")
  public static class MongoConstraintNameResolverConfiguration {

    @Bean
    ConstraintNameResolver mongoConstraintNameResolver(final Environment env) {
      return new MongoConstraintNameResolver();
    }
  }
}
