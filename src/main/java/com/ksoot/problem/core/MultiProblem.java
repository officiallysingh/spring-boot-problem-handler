package com.ksoot.problem.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/** A {@link RuntimeException} that can hold multiple problems or exceptions. */
@Getter
public class MultiProblem extends RuntimeException {

  private final HttpStatus status;

  private final List<Object> errors;

  /**
   * Constructs a new multi-problem with the given status and problems.
   *
   * @param status the HTTP status
   * @param problems the list of problems
   */
  private MultiProblem(final HttpStatus status, final List<Problem> problems) {
    super(status.getReasonPhrase());
    Assert.isTrue(CollectionUtils.isNotEmpty(problems), "'problems' must not be null or empty");
    Assert.noNullElements(problems, "'problems' must not contain null");
    this.status = status;
    this.errors = new ArrayList<>(problems);
  }

  /**
   * Constructs a new multi-problem with the given exceptions and status.
   *
   * @param exceptions the list of exceptions
   * @param status the HTTP status
   */
  private MultiProblem(final List<Throwable> exceptions, final HttpStatus status) {
    super(status.getReasonPhrase());
    Assert.isTrue(CollectionUtils.isNotEmpty(exceptions), "'exceptions' must not be null or empty");
    Assert.noNullElements(exceptions, "'exceptions' must not contain null");
    this.status = status;
    this.errors = new ArrayList<>(exceptions);
  }

  /**
   * Creates a {@link MultiProblem} from a list of exceptions.
   *
   * @param status the HTTP status
   * @param exceptions the list of exceptions
   * @return a new multi-problem
   */
  public static MultiProblem ofExceptions(
      final HttpStatus status, final List<Throwable> exceptions) {
    Assert.notNull(status, "'status' must not be null");
    return new MultiProblem(exceptions, status);
  }

  /**
   * Creates a {@link MultiProblem} from a list of exceptions with {@link HttpStatus#MULTI_STATUS}.
   *
   * @param exceptions the list of exceptions
   * @return a new multi-problem
   */
  public static MultiProblem ofExceptions(final List<Throwable> exceptions) {
    return ofExceptions(HttpStatus.MULTI_STATUS, exceptions);
  }

  /**
   * Creates a {@link MultiProblem} from a list of problems.
   *
   * @param status the HTTP status
   * @param problems the list of problems
   * @return a new multi-problem
   */
  public static MultiProblem ofProblems(final HttpStatus status, final List<Problem> problems) {
    Assert.notNull(status, "'status' must not be null");
    return new MultiProblem(status, problems);
  }

  /**
   * Creates a {@link MultiProblem} from a list of problems with {@link HttpStatus#MULTI_STATUS}.
   *
   * @param problems the list of problems
   * @return a new multi-problem
   */
  public static MultiProblem ofProblems(final List<Problem> problems) {
    return ofProblems(HttpStatus.MULTI_STATUS, problems);
  }

  /**
   * Adds an exception to this multi-problem.
   *
   * @param exception the exception to add
   * @return this multi-problem
   */
  public MultiProblem add(final Throwable exception) {
    Assert.notNull(exception, "'exception' must not be null");
    this.errors.add(exception);
    return this;
  }

  /**
   * Adds a problem to this multi-problem.
   *
   * @param problem the problem to add
   * @return this multi-problem
   */
  public MultiProblem add(final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    this.errors.add(problem);
    return this;
  }

  /**
   * Returns the list of errors.
   *
   * @return an unmodifiable list of errors
   */
  public List<Object> getErrors() {
    return Collections.unmodifiableList(this.errors);
  }
}
