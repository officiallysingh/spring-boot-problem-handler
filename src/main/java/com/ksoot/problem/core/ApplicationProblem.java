package com.ksoot.problem.core;

import jakarta.annotation.Nullable;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * A {@link RuntimeException} that implements {@link ProblemSupport}. Used to throw problems that
 * should be handled by the problem handler.
 */
@Getter
public final class ApplicationProblem extends RuntimeException implements ProblemSupport {

  private final HttpStatus status;
  private final String errorKey;
  private final String defaultDetail;
  private final Object[] detailArgs;

  private final ThrowableProblem cause;
  private final Map<String, Object> parameters;

  private final Problem problem;

  /**
   * Constructs a new application problem.
   *
   * @param message the message
   * @param status the HTTP status
   * @param problem the problem
   * @param errorKey the error key
   * @param defaultDetail the default detail
   * @param detailArgs the detail arguments
   * @param cause the cause problem
   * @param parameters additional parameters
   */
  private ApplicationProblem(
      final String message,
      final HttpStatus status,
      final Problem problem,
      final String errorKey,
      @Nullable final String defaultDetail,
      @Nullable final Object[] detailArgs,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    super(message, cause);
    Assert.notNull(status, "'status' must not be null");
    this.status = status;
    this.errorKey = errorKey;
    this.problem = problem;
    this.defaultDetail = defaultDetail;
    this.detailArgs = detailArgs;
    this.cause = cause;
    this.parameters = parameters;
  }

  /**
   * Creates an {@link ApplicationProblem} with the given status and error details.
   *
   * @param status the HTTP status
   * @param errorKey the error key
   * @param defaultDetail the default detail message
   * @param detailArgs the detail message arguments
   * @param cause the cause
   * @param parameters additional parameters
   * @return a new application problem
   */
  public static ApplicationProblem of(
      final HttpStatus status,
      final String errorKey,
      @Nullable final String defaultDetail,
      @Nullable final Object[] detailArgs,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    Assert.hasText(errorKey, "'errorKey' must not be null or empty");
    return new ApplicationProblem(
        ProblemUtils.toMessage(errorKey, defaultDetail, detailArgs, null, cause),
        status,
        null,
        errorKey,
        defaultDetail,
        detailArgs,
        cause,
        parameters);
  }

  /**
   * Creates an {@link ApplicationProblem} with the given status and error key.
   *
   * @param status the HTTP status
   * @param errorKey the error key
   * @return a new application problem
   */
  public static ApplicationProblem of(final HttpStatus status, final String errorKey) {
    Assert.hasText(errorKey, "'errorKey' must not be null or empty");
    return new ApplicationProblem(
        ProblemUtils.toMessage(errorKey, null, null, null, null),
        status,
        null,
        errorKey,
        null,
        null,
        null,
        null);
  }

  /**
   * Creates an {@link ApplicationProblem} from an existing {@link Problem}.
   *
   * @param status the HTTP status
   * @param problem the problem
   * @return a new application problem
   */
  public static ApplicationProblem of(final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return new ApplicationProblem(
        ProblemUtils.toMessage(null, null, null, problem, null),
        status,
        problem,
        null,
        problem.getDetail(),
        null,
        null,
        null);
  }
}
