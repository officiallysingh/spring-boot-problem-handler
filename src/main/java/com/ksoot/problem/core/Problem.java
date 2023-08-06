package com.ksoot.problem.core;

import jakarta.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

/**
 * {@link Problem} instances are required to be immutable.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807: Problem Details
 * for HTTP APIs</a>
 */
public interface Problem {

  String getCode();

  /**
   * A short, human-readable summary of the problem type. It SHOULD NOT change
   * from occurrence to occurrence of the problem, except for purposes of
   * localisation.
   *
   * @return a short, human-readable summary of this problem
   */
  String getTitle();

  /**
   * A human readable explanation specific to this occurrence of the problem.
   *
   * @return A human readable explanation of this problem
   */
  String getDetail();

  ThrowableProblem getCause();

  /**
   * Optional, additional attributes of the problem. Implementations can choose to
   * ignore this in favor of concrete, typed fields.
   *
   * @return additional parameters
   */
  default Map<String, Object> getParameters() {
    return Collections.emptyMap();
  }

  static String toString(final Problem problem) {
    final Stream<String> parts = Stream
        .concat(Stream.of(problem.getCode(), problem.getTitle(), problem.getDetail()),
            problem.getParameters().entrySet().stream().map(Map.Entry::toString))
        .filter(Objects::nonNull);

    return problem.getCode() + "{" + parts.collect(joining(", ")) + "}";
  }

  // ------------- Build Problems ---------------
  static TitleBuilder code(final String code) {
    return new Builder(code);
  }

  static DetailBuilder of(final HttpStatus status) {
    return new Builder("" + status.value(), status.getReasonPhrase());
  }

  static CauseBuilder of(final String code, final String title, final String detail) {
    return new Builder(code, title, detail);
  }

  public interface TitleBuilder {
    DetailBuilder title(final String title);
  }

  public interface DetailBuilder {
    CauseBuilder detail(final String detail);
  }

  public interface CauseBuilder extends ParameterBuilder {
    ParameterBuilder cause(@Nullable ThrowableProblem cause);
  }

  public interface ParameterBuilder extends ParametersBuilder {
    ParameterBuilder parameter(final String key, final Object value);
  }

  public interface ParametersBuilder extends org.apache.commons.lang3.builder.Builder<ThrowableProblem> {
    org.apache.commons.lang3.builder.Builder<ThrowableProblem> parameters(@Nullable final Map<String, Object> parameters);
  }

  public static class Builder implements TitleBuilder, DetailBuilder, CauseBuilder {

    private static final Set<String> RESERVED_PROPERTIES = new HashSet<>(
        Arrays.asList("code", "title", "detail", "cause"));
    private String code;
    private String title;
    private String detail;
    private ThrowableProblem cause;
    private final Map<String, Object> parameters = new LinkedHashMap<>();

    Builder(final String code) {
      Assert.hasText(code, "'code' must not be null or empty");
      this.code = code;
    }

    Builder(final String code, final String title) {
      this(code);
      Assert.hasText(title, "'title' must not be null or empty");
      this.title = title;
    }

    Builder(final String code, final String title, final String detail) {
      this(code, title);
      Assert.hasText(detail, "'detail' must not be null or empty");
      this.detail = detail;
    }

    Builder(final String code, final String title, final String detail,
            final ThrowableProblem cause) {
      this(code, title, detail);
      this.cause = cause;
    }

    Builder(final String code, final String title, final String detail,
            final ThrowableProblem cause, final Map<String, Object> parameters) {
      this(code, title, detail, cause);
      if (MapUtils.isNotEmpty(parameters)) {
        this.parameters.putAll(parameters);
      }
    }

    @Override
    public DetailBuilder title(final String title) {
      Assert.hasText(title, "'title' must not be null or empty");
      this.title = title;
      return this;
    }

    @Override
    public CauseBuilder detail(final String detail) {
      Assert.hasText(detail, "'detail' must not be null or empty");
      this.detail = detail;
      return this;
    }

    @Override
    public ParameterBuilder cause(@Nullable final ThrowableProblem cause) {
      this.cause = cause;
      return this;
    }

    @Override
    public ParameterBuilder parameter(final String key, final Object value) {
      Assert.hasText(key, "'key' must not be null or empty");
      Assert.isTrue(!RESERVED_PROPERTIES.contains(key), "Property " + key + " is reserved");
      this.parameters.put(key, value);
      return this;
    }

    @Override
    public org.apache.commons.lang3.builder.Builder<ThrowableProblem> parameters(
        @Nullable final Map<String, Object> parameters) {
      if (MapUtils.isNotEmpty(parameters)) {
        parameters.entrySet().stream().forEach(entry -> parameter(entry.getKey(), entry.getValue()));
      }
      return this;
    }

    @Override
    public ThrowableProblem build() {
      return new DefaultProblem(this.code, this.title, this.detail, this.cause, this.parameters);
    }
  }
}
