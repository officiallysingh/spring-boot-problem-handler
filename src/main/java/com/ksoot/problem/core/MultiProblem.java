package com.ksoot.problem.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

@Getter
public class MultiProblem extends RuntimeException {

  private final HttpStatus status;

  private final List<Object> errors;

  private MultiProblem(final HttpStatus status, final List<Problem> problems) {
    super(status.getReasonPhrase());
    Assert.isTrue(CollectionUtils.isNotEmpty(problems), "'problems' must not be null or empty");
    Assert.noNullElements(problems, "'problems' must not contain null");
    this.status = status;
    this.errors = new ArrayList<>(problems);
  }

  private MultiProblem(final List<Throwable> exceptions, final HttpStatus status) {
    super(status.getReasonPhrase());
    Assert.isTrue(CollectionUtils.isNotEmpty(exceptions), "'exceptions' must not be null or empty");
    Assert.noNullElements(exceptions, "'exceptions' must not contain null");
    this.status = status;
    this.errors = new ArrayList<>(exceptions);
  }

  public static MultiProblem ofExceptions(
      final HttpStatus status, final List<Throwable> exceptions) {
    Assert.notNull(status, "'status' must not be null");
    return new MultiProblem(exceptions, status);
  }

  public static MultiProblem ofExceptions(final List<Throwable> exceptions) {
    return ofExceptions(HttpStatus.MULTI_STATUS, exceptions);
  }

  public static MultiProblem ofProblems(final HttpStatus status, final List<Problem> problems) {
    Assert.notNull(status, "'status' must not be null");
    return new MultiProblem(status, problems);
  }

  public static MultiProblem ofProblems(final List<Problem> problems) {
    return ofProblems(HttpStatus.MULTI_STATUS, problems);
  }

  public MultiProblem add(final Throwable exception) {
    Assert.notNull(exception, "'exception' must not be null");
    this.errors.add(exception);
    return this;
  }

  public MultiProblem add(final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    this.errors.add(problem);
    return this;
  }

  public List<Object> getErrors() {
    return Collections.unmodifiableList(this.errors);
  }
}
