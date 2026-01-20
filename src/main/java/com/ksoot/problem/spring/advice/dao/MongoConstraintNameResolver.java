package com.ksoot.problem.spring.advice.dao;

import com.ksoot.problem.core.ProblemConstant;

/**
 * {@link ConstraintNameResolver} implementation for MongoDB.
 *
 * @author Rajveer Singh
 */
public class MongoConstraintNameResolver implements ConstraintNameResolver {

  /** {@inheritDoc} */
  @Override
  public String resolveConstraintName(final String exceptionMessage) {
    String exMessage = exceptionMessage.trim();
    try {
      String temp = exMessage.substring(exMessage.indexOf("collection: ") + 12);
      String collectionName = temp.substring(temp.indexOf(".") + 1, temp.indexOf(" index: "));
      temp = exMessage.substring(exMessage.indexOf("index: ") + 7);
      String indexName = temp.substring(0, temp.indexOf(" "));
      return collectionName + ProblemConstant.DOT + indexName;
    } catch (final Exception e) {
      // Ignored on purpose
    }
    return "mongo.duplicate.key";
  }

  /** {@inheritDoc} */
  @Override
  public DBType dbType() {
    return DBType.MONGO_DB;
  }
}
