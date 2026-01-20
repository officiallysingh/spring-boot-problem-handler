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

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for DAO
 * exception handling.
 */
@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(value = {DaoAdviceEnabled.class, ORMAdviceEnabled.class})
@ConditionalOnWebApplication
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class ProblemDaoConfiguration {

  /** Configuration for PostgreSQL constraint name resolution. */
  @ConditionalOnMissingBean(name = "postgresqlConstraintNameResolver")
  @Conditional(ORMUrlAvailable.class)
  @ConditionalOnProperty(prefix = "spring.jpa", name = "database", havingValue = "POSTGRESQL")
  static class PostgresqlConstraintNameResolverConfiguration {

    /**
     * Creates a {@link PostgresConstraintNameResolver} bean.
     *
     * @param env the application environment
     * @return the PostgreSQL constraint name resolver
     */
    @Bean
    ConstraintNameResolver postgresqlConstraintNameResolver(final Environment env) {
      return new PostgresConstraintNameResolver();
    }
  }

  /** Configuration for SQL Server constraint name resolution. */
  @ConditionalOnMissingBean(name = "sqlServerConstraintNameResolver")
  @Conditional(ORMUrlAvailable.class)
  @ConditionalOnProperty(prefix = "spring.jpa", name = "database", havingValue = "SQL_SERVER")
  static class SQLServerConstraintNameResolverConfiguration {

    /**
     * Creates a {@link SQLServerConstraintNameResolver} bean.
     *
     * @param env the application environment
     * @return the SQL Server constraint name resolver
     */
    @Bean
    ConstraintNameResolver sqlServerConstraintNameResolver(final Environment env) {
      return new SQLServerConstraintNameResolver();
    }
  }

  /** Configuration for MongoDB constraint name resolution. */
  @ConditionalOnClass(value = {MongoDatabaseFactory.class})
  @ConditionalOnProperty(prefix = "spring.mongodb", name = "uri")
  @ConditionalOnMissingBean(name = "mongoConstraintNameResolver")
  static class MongoConstraintNameResolverConfiguration {

    /**
     * Creates a {@link MongoConstraintNameResolver} bean.
     *
     * @param env the application environment
     * @return the MongoDB constraint name resolver
     */
    @Bean
    ConstraintNameResolver mongoConstraintNameResolver(final Environment env) {
      return new MongoConstraintNameResolver();
    }
  }
}
