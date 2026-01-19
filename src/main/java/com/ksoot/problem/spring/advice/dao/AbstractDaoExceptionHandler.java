package com.ksoot.problem.spring.advice.dao;

import com.ksoot.problem.spring.config.ProblemConfigException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

public abstract class AbstractDaoExceptionHandler<T, R> implements DaoAdviceTraits<T, R> {

  protected final Map<DBType, ConstraintNameResolver> constraintNameResolvers;

  protected final Database database;

  protected AbstractDaoExceptionHandler(
      final List<ConstraintNameResolver> constraintNameResolvers, final Environment env) {
    if (CollectionUtils.isNotEmpty(constraintNameResolvers)) {
      this.constraintNameResolvers =
          constraintNameResolvers.stream()
              .collect(Collectors.toMap(ConstraintNameResolver::dbType, Function.identity()));
    } else {
      this.constraintNameResolvers = Collections.emptyMap();
    }

    String dbPlatform = env.getProperty("spring.jpa.database");
    if (this.constraintNameResolvers.containsKey(DBType.POSTGRESQL)
        || this.constraintNameResolvers.containsKey(DBType.SQL_SERVER)
        || this.constraintNameResolvers.containsKey(DBType.MYSQL)
        || this.constraintNameResolvers.containsKey(DBType.ORACLE)) {
      if (StringUtils.isEmpty(dbPlatform)) {
        throw new ProblemConfigException(
            "Property \"spring.jpa.database\" not found. Please specify database plateform in configurations");
      } else {
        this.database = Database.valueOf(dbPlatform);
      }
    } else {
      this.database = null;
    }
  }

  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    if (exceptionMessage.contains("WriteError")) { // MongoDB constraint violation
      return this.constraintNameResolvers
          .get(DBType.MONGO_DB)
          .resolveConstraintName(exceptionMessage);
    } else {
      return switch (this.database) {
        case SQL_SERVER ->
            this.constraintNameResolvers
                .get(DBType.SQL_SERVER)
                .resolveConstraintName(exceptionMessage);
        case POSTGRESQL ->
            this.constraintNameResolvers
                .get(DBType.POSTGRESQL)
                .resolveConstraintName(exceptionMessage);
        case MYSQL ->
            this.constraintNameResolvers.get(DBType.MYSQL).resolveConstraintName(exceptionMessage);
        case ORACLE ->
            this.constraintNameResolvers.get(DBType.ORACLE).resolveConstraintName(exceptionMessage);
        // TODO: Add more cases for other databases constraint name resolver
        // implementations
        default ->
            throw new IllegalStateException(
                "constraintNameResolver bean could not be found, "
                    + "add ConstraintNameResolver implementation for: "
                    + this.database);
      };
    }
  }
}
