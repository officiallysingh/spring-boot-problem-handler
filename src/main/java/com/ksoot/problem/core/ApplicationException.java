package com.ksoot.problem.core;

import jakarta.annotation.Nullable;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * A checked {@link Exception} that implements {@link ProblemSupport}. Used to throw problems that
 * should be handled by the problem handler.
 */
@Getter
public final class ApplicationException extends Exception implements ProblemSupport {

  private final HttpStatus status;
  private final String errorKey;
  private final String defaultDetail;
  private final Object[] detailArgs;

  private final ThrowableProblem cause;
  private final Map<String, Object> parameters;

  private final Problem problem;

  private ApplicationException(
      final String message,
      final HttpStatus status,
      final Problem problem,
      final String errorKey,
      @Nullable final String defaultDetail,
      @Nullable final Object[] detailArgs,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    super(message, cause);
    this.status = status;
    this.errorKey = errorKey;
    this.problem = problem;
    this.defaultDetail = defaultDetail;
    this.detailArgs = detailArgs;
    this.cause = cause;
    this.parameters = parameters;
  }

  /**
   * Creates an {@link ApplicationException} with the given status and error details.
   *
   * @param status the HTTP status
   * @param errorKey the error key
   * @param defaultDetail the default detail message
   * @param detailArgs the detail message arguments
   * @param cause the cause
   * @param parameters additional parameters
   * @return a new application exception
   */
  public static ApplicationException of(
      final HttpStatus status,
      final String errorKey,
      @Nullable final String defaultDetail,
      @Nullable final Object[] detailArgs,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    Assert.hasText(errorKey, "'errorKey' must not be null or empty");
    return new ApplicationException(
        ProblemUtils.toMessage(errorKey, defaultDetail, null, cause),
        status,
        null,
        errorKey,
        defaultDetail,
        detailArgs,
        cause,
        parameters);
  }

  /**
   * Creates an {@link ApplicationException} from an existing {@link Problem}.
   *
   * @param status the HTTP status
   * @param problem the problem
   * @return a new application exception
   */
  public static ApplicationException of(final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return new ApplicationException(
        ProblemUtils.toMessage(null, null, problem, null),
        status,
        problem,
        null,
        null,
        null,
        null,
        null);
  }
}
