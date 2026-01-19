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

@UtilityClass
public class Problems {

  // ------------- Factory methods -------------
  public static Problem.DetailBuilder newInstance(final HttpStatus status) {
    return new Problem.ProblemBuilder(String.valueOf(status.value()), status.getReasonPhrase());
  }

  public static Problem.CauseBuilder newInstance(
      final String code, final String title, final String detail) {
    return new Problem.ProblemBuilder(code, title, detail);
  }

  public static DefaultDetailBuilder newInstance(final String errorKey) {
    return new Builder(errorKey);
  }

  public static DetailArgsBuilder newInstance(final ErrorType errorType) {
    return new Builder(
        errorType.getErrorKey(), errorType.getDefaultDetail(), errorType.getStatus());
  }

  // Utility methods to prepare throwables from existing problem instances
  public static ApplicationProblem throwAble(final HttpStatus status, final Problem problem) {
    return ApplicationProblem.of(status, problem);
  }

  public static ApplicationException throwAbleChecked(
      final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return ApplicationException.of(status, problem);
  }

  public static MultiProblem ofExceptions(
      final HttpStatus status, final List<Throwable> exceptions) {
    return MultiProblem.ofExceptions(status, exceptions);
  }

  public static MultiProblem ofExceptions(final HttpStatus status, final Throwable... exceptions) {
    Assert.isTrue(ArrayUtils.isNotEmpty(exceptions), "'exceptions' must not be null or empty");
    Assert.noNullElements(exceptions, "'exceptions' must not contain null");
    return MultiProblem.ofExceptions(status, Arrays.asList(exceptions));
  }

  public static MultiProblem of(final HttpStatus status, final List<Problem> problems) {
    Assert.notNull(status, "'status' must not be null");
    return MultiProblem.ofProblems(status, problems);
  }

  public static MultiProblem of(final HttpStatus status, final Problem... problems) {
    Assert.isTrue(ArrayUtils.isNotEmpty(problems), "'problems' must not be null or empty");
    Assert.noNullElements(problems, "'problems' must not contain null");
    return MultiProblem.ofProblems(status, Arrays.asList(problems));
  }

  public static ApplicationProblem notFound() {
    return newInstance(GeneralErrorKey.NOT_FOUND).throwAble(NOT_FOUND);
  }

  public static ApplicationProblem internalServerError() {
    return newInstance(GeneralErrorKey.INTERNAL_SERVER_ERROR).throwAble(INTERNAL_SERVER_ERROR);
  }

  // ----------- Builder -----------
  public interface DefaultDetailBuilder extends DetailArgsBuilder {
    DetailArgsBuilder defaultDetail(@Nullable final String detail);
  }

  public interface DetailArgsBuilder extends CauseBuilder {
    CauseBuilder detailArgs(@Nullable final Object... detailArgs);
  }

  public interface CauseBuilder extends ParameterBuilder {
    ParameterBuilder cause(@Nullable final Throwable cause);
  }

  public interface ParameterBuilder extends ParametersBuilder {
    ParameterBuilder parameter(final String key, final Object value);
  }

  public interface ParametersBuilder extends ProblemBuildable {
    ProblemBuildable parameters(@Nullable final Map<String, Object> parameters);
  }

  public interface ProblemBuildable {

    ApplicationProblem throwAble(final HttpStatus status);

    ApplicationProblem throwAble();

    ApplicationException throwAbleChecked(final HttpStatus status);

    ApplicationException throwAbleChecked();
  }

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

    private Map<String, Object> parameters = new LinkedHashMap<>(4);

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
        parameters.entrySet().forEach(entry -> parameter(entry.getKey(), entry.getValue()));
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
