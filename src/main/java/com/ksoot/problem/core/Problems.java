package com.ksoot.problem.core;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/** Factory class for creating problem instances. */
@UtilityClass
public class Problems {

  // ------------- Factory methods -------------

  /**
   * Creates a new {@link Problem.DetailBuilder} for the given {@link HttpStatus}.
   *
   * @param status the HTTP status
   * @return a new detail builder
   */
  public static Problem.DetailBuilder newInstance(final HttpStatus status) {
    return new Problem.ProblemBuilder(String.valueOf(status.value()), status.getReasonPhrase());
  }

  /**
   * Creates a new {@link Problem.CauseBuilder} with the given code, title, and detail.
   *
   * @param code the problem code
   * @param title the problem title
   * @param detail the problem detail
   * @return a new cause builder
   */
  public static Problem.CauseBuilder newInstance(
      final String code, final String title, final String detail) {
    return new Problem.ProblemBuilder(code, title, detail);
  }

  /**
   * Creates a new {@link DefaultDetailBuilder} for the given error key.
   *
   * @param errorKey the error key
   * @return a new default detail builder
   */
  public static DefaultDetailBuilder newInstance(final String errorKey) {
    return new Builder(errorKey);
  }

  /**
   * Creates a new {@link DetailArgsBuilder} for the given {@link ErrorType}.
   *
   * @param errorType the error type
   * @return a new detail args builder
   */
  public static DetailArgsBuilder newInstance(final ErrorType errorType) {
    return new Builder(
        errorType.getErrorKey(), errorType.getDefaultDetail(), errorType.getStatus());
  }

  // Utility methods to prepare throwables from existing problem instances

  /**
   * Creates an {@link ApplicationProblem} from an existing {@link Problem}.
   *
   * @param status the HTTP status
   * @param problem the problem
   * @return a new application problem
   */
  public static ApplicationProblem throwAble(final HttpStatus status, final Problem problem) {
    return ApplicationProblem.of(status, problem);
  }

  /**
   * Creates an {@link ApplicationException} from an existing {@link Problem}.
   *
   * @param status the HTTP status
   * @param problem the problem
   * @return a new application exception
   */
  public static ApplicationException throwAbleChecked(
      final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return ApplicationException.of(status, problem);
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
    return MultiProblem.ofExceptions(status, exceptions);
  }

  /**
   * Creates a {@link MultiProblem} from an array of exceptions.
   *
   * @param status the HTTP status
   * @param exceptions the exceptions
   * @return a new multi-problem
   */
  public static MultiProblem ofExceptions(final HttpStatus status, final Throwable... exceptions) {
    Assert.isTrue(ArrayUtils.isNotEmpty(exceptions), "'exceptions' must not be null or empty");
    Assert.noNullElements(exceptions, "'exceptions' must not contain null");
    return MultiProblem.ofExceptions(status, Arrays.asList(exceptions));
  }

  /**
   * Creates a {@link MultiProblem} from a list of problems.
   *
   * @param status the HTTP status
   * @param problems the list of problems
   * @return a new multi-problem
   */
  public static MultiProblem of(final HttpStatus status, final List<Problem> problems) {
    Assert.notNull(status, "'status' must not be null");
    return MultiProblem.ofProblems(status, problems);
  }

  /**
   * Creates a {@link MultiProblem} from an array of problems.
   *
   * @param status the HTTP status
   * @param problems the problems
   * @return a new multi-problem
   */
  public static MultiProblem of(final HttpStatus status, final Problem... problems) {
    Assert.isTrue(ArrayUtils.isNotEmpty(problems), "'problems' must not be null or empty");
    Assert.noNullElements(problems, "'problems' must not contain null");
    return MultiProblem.ofProblems(status, Arrays.asList(problems));
  }

  /**
   * Creates an {@link ApplicationProblem} for a NOT FOUND error.
   *
   * @return a not found application problem
   */
  public static ApplicationProblem notFound() {
    return newInstance(GeneralErrorKey.NOT_FOUND).throwAble(NOT_FOUND);
  }

  /**
   * Creates an {@link ApplicationProblem} for an INTERNAL SERVER ERROR.
   *
   * @return an internal server error application problem
   */
  public static ApplicationProblem internalServerError() {
    return newInstance(GeneralErrorKey.INTERNAL_SERVER_ERROR).throwAble(INTERNAL_SERVER_ERROR);
  }

  // ----------- Builder -----------

  /** Builder step for setting the default detail. */
  public interface DefaultDetailBuilder extends DetailArgsBuilder {
    /**
     * Sets the default detail.
     *
     * @param detail the default detail
     * @return the next step in the builder
     */
    DetailArgsBuilder defaultDetail(@Nullable final String detail);
  }

  /** Builder step for setting detail arguments. */
  public interface DetailArgsBuilder extends CauseBuilder {
    /**
     * Sets the detail arguments for localized messages.
     *
     * @param detailArgs the detail arguments
     * @return the next step in the builder
     */
    CauseBuilder detailArgs(@Nullable final Object... detailArgs);
  }

  /** Builder step for setting the cause. */
  public interface CauseBuilder extends ParameterBuilder {
    /**
     * Sets the cause of the problem.
     *
     * @param cause the cause
     * @return the next step in the builder
     */
    ParameterBuilder cause(@Nullable final Throwable cause);
  }

  /** Builder step for setting additional parameters. */
  public interface ParameterBuilder extends ParametersBuilder {
    /**
     * Adds an additional parameter.
     *
     * @param key the parameter key
     * @param value the parameter value
     * @return this builder
     */
    ParameterBuilder parameter(final String key, final Object value);
  }

  /** Builder step for setting multiple parameters at once. */
  public interface ParametersBuilder extends ProblemBuildable {
    /**
     * Adds multiple additional parameters.
     *
     * @param parameters the parameters
     * @return this builder
     */
    ProblemBuildable parameters(@Nullable final Map<String, Object> parameters);
  }

  /** Final step for building the throwable problem. */
  public interface ProblemBuildable {

    /**
     * Builds and returns an {@link ApplicationProblem} with the given status.
     *
     * @param status the HTTP status
     * @return a new application problem
     */
    ApplicationProblem throwAble(final HttpStatus status);

    /**
     * Builds and returns an {@link ApplicationProblem} with the default status.
     *
     * @return a new application problem
     */
    ApplicationProblem throwAble();

    /**
     * Builds and returns an {@link ApplicationException} with the given status.
     *
     * @param status the HTTP status
     * @return a new application exception
     */
    ApplicationException throwAbleChecked(final HttpStatus status);

    /**
     * Builds and returns an {@link ApplicationException} with the default status.
     *
     * @return a new application exception
     */
    ApplicationException throwAbleChecked();
  }

  /** Implementation of the problem builder. */
  public static class Builder implements DefaultDetailBuilder {

    private static final Set<String> RESERVED_PROPERTIES =
        new HashSet<>(
            Arrays.asList(
                "code",
                "title",
                "detail",
                "cause",
                ProblemConstant.CODE_RESOLVER,
                ProblemConstant.TITLE_RESOLVER,
                ProblemConstant.DETAIL_RESOLVER));

    private final String errorKey;
    private String defaultDetail;
    private HttpStatus status;
    private Object[] detailArgs;

    private ThrowableProblem cause;

    private final Map<String, Object> parameters = new LinkedHashMap<>(4);

    Builder(final String errorKey) {
      this.errorKey = errorKey;
    }

    Builder(final String errorKey, final String defaultDetail, final HttpStatus status) {
      this.errorKey = errorKey;
      this.defaultDetail = defaultDetail;
      this.status = status;
    }

    @Override
    public DetailArgsBuilder defaultDetail(@Nullable final String defaultDetail) {
      this.defaultDetail = defaultDetail;
      return this;
    }

    @Override
    public CauseBuilder detailArgs(@Nullable final Object... detailArgs) {
      this.detailArgs = detailArgs;
      return this;
    }

    @Override
    public ParameterBuilder cause(@Nullable final Throwable cause) {
      this.cause = cause != null ? ProblemUtils.toProblem(cause) : null;
      return this;
    }

    @Override
    public ParameterBuilder parameter(final String key, final Object value) {
      Assert.hasLength(key, "'key' must not be null or empty");
      Assert.isTrue(!RESERVED_PROPERTIES.contains(key), "Property " + key + " is reserved");
      this.parameters.put(key, value);
      return this;
    }

    @Override
    public ProblemBuildable parameters(@Nullable final Map<String, Object> parameters) {
      if (MapUtils.isNotEmpty(parameters)) {
        parameters.forEach((key, value) -> parameter(key, value));
      }
      return this;
    }

    @Override
    public ApplicationProblem throwAble() {
      return ApplicationProblem.of(
          Optional.ofNullable(this.status).orElse(INTERNAL_SERVER_ERROR),
          this.errorKey,
          this.defaultDetail,
          this.detailArgs,
          this.cause,
          this.parameters);
    }

    @Override
    public ApplicationProblem throwAble(final HttpStatus status) {
      return ApplicationProblem.of(
          status, this.errorKey, this.defaultDetail, this.detailArgs, this.cause, this.parameters);
    }

    @Override
    public ApplicationException throwAbleChecked() {
      return ApplicationException.of(
          Optional.ofNullable(this.status).orElse(INTERNAL_SERVER_ERROR),
          this.errorKey,
          this.defaultDetail,
          this.detailArgs,
          this.cause,
          this.parameters);
    }

    @Override
    public ApplicationException throwAbleChecked(final HttpStatus status) {
      return ApplicationException.of(
          status, this.errorKey, this.defaultDetail, this.detailArgs, this.cause, this.parameters);
    }
  }
}
