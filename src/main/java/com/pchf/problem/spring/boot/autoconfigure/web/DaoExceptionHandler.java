package com.pchf.problem.spring.boot.autoconfigure.web;

import com.pchf.problem.ProblemDetails;
import com.pchf.problem.spring.advice.dao.ConstraintNameResolver;
import com.pchf.problem.spring.advice.dao.DBType;
import com.pchf.problem.spring.advice.dao.DaoAdviceTraits;
import com.pchf.problem.spring.boot.autoconfigure.DaoAdviceEnabled;
import com.pchf.problem.spring.boot.autoconfigure.ORMAdviceEnabled;
import com.pchf.problem.spring.boot.autoconfigure.ProblemProperties;
import com.pchf.problem.spring.config.ProblemConfigException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(value = {DaoAdviceEnabled.class, ORMAdviceEnabled.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class DaoExceptionHandler implements DaoAdviceTraits<NativeWebRequest, ResponseEntity<ProblemDetails>> {

  private final Map<DBType, ConstraintNameResolver> constraintNameResolvers;

  private final Database database;

  DaoExceptionHandler(final List<ConstraintNameResolver> constraintNameResolvers,
                      final Environment env) {
    if (CollectionUtils.isNotEmpty(constraintNameResolvers)) {
      this.constraintNameResolvers = constraintNameResolvers.stream()
          .collect(Collectors.toMap(ConstraintNameResolver::dbType, Function.identity()));
    } else {
      this.constraintNameResolvers = Collections.EMPTY_MAP;
    }

    String dbPlateform = env.getProperty("spring.jpa.database");
    if (this.constraintNameResolvers.containsKey(DBType.POSTGRESQL)
        || this.constraintNameResolvers.containsKey(DBType.SQL_SERVER)
        || this.constraintNameResolvers.containsKey(DBType.MYSQL)
        || this.constraintNameResolvers.containsKey(DBType.ORACLE)) {
      if (StringUtils.isEmpty(dbPlateform)) {
        throw new ProblemConfigException(
            "Property \"spring.jpa.database\" not found. Please specify database plateform in configurations");
      } else {
        this.database = Database.valueOf(dbPlateform);
      }
    } else {
      this.database = null;
    }
  }

  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    if (exceptionMessage.contains("WriteError")) { // MongoDB constraint violation
      return this.constraintNameResolvers.get(DBType.MONGO_DB).resolveConstraintName(exceptionMessage);
    } else {
      switch (this.database) {
        case SQL_SERVER:
          return this.constraintNameResolvers.get(DBType.SQL_SERVER).resolveConstraintName(exceptionMessage);
        case POSTGRESQL:
          return this.constraintNameResolvers.get(DBType.POSTGRESQL).resolveConstraintName(exceptionMessage);
        case MYSQL:
          return this.constraintNameResolvers.get(DBType.MYSQL).resolveConstraintName(exceptionMessage);
        case ORACLE:
          return this.constraintNameResolvers.get(DBType.ORACLE).resolveConstraintName(exceptionMessage);
        // TODO: Add more cases for other databases constraint name resolver
        // implementations
        default:
          throw new IllegalStateException("constraintNameResolver bean could not be found, "
              + "add ConstraintNameResolver implementation for: " + this.database);
      }
    }
  }
}
