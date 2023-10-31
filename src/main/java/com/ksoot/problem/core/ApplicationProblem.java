package com.ksoot.problem.core;

import jakarta.annotation.Nullable;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

@Getter
@SuppressWarnings("serial")
public final class ApplicationProblem extends RuntimeException implements ProblemSupport {

  private final HttpStatus status;
  private final String errorKey;
  private final String defaultDetail;
  private final Object[] detailArgs;

  private final ThrowableProblem cause;
  private final Map<String, Object> parameters;

  private final Problem problem;

  private ApplicationProblem(
      final HttpStatus status,
      final Problem problem,
      final String errorKey,
      @Nullable final String defaultDetail,
      @Nullable final Object[] detailArgs,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    super(defaultDetail, cause);
    Assert.notNull(status, "'status' must not be null");
    this.status = status;
    this.errorKey = errorKey;
    this.problem = problem;
    this.defaultDetail = defaultDetail;
    this.detailArgs = detailArgs;
    this.cause = cause;
    this.parameters = parameters;
  }

  public static ApplicationProblem of(
      final HttpStatus status,
      final String errorKey,
      @Nullable final String defaultDetail,
      @Nullable final Object[] detailArgs,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    Assert.hasText(errorKey, "'errorKey' must not be null or empty");
    return new ApplicationProblem(
        status, null, errorKey, defaultDetail, detailArgs, cause, parameters);
  }

  public static ApplicationProblem of(final HttpStatus status, final String errorKey) {
    Assert.hasText(errorKey, "'errorKey' must not be null or empty");
    return new ApplicationProblem(status, null, errorKey, null, null, null, null);
  }

  public static ApplicationProblem of(final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return new ApplicationProblem(status, problem, null, problem.getDetail(), null, null, null);
  }
}
