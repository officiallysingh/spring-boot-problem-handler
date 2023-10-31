package com.ksoot.problem.core;

import static java.util.stream.Collectors.joining;

import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.collections4.MapUtils;
import org.springframework.util.Assert;

/**
 * {@link Problem} instances are required to be immutable.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7807">RFC 7807: Problem Details for HTTP APIs</a>
 */
public interface Problem {

  String getCode();

  /**
   * A short, human-readable summary of the problem type. It SHOULD NOT change from occurrence to
   * occurrence of the problem, except for purposes of localisation.
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
   * Optional, additional attributes of the problem. Implementations can choose to ignore this in
   * favor of concrete, typed fields.
   *
   * @return additional parameters
   */
  default Map<String, Object> getParameters() {
    return Collections.emptyMap();
  }

  static String toString(final Problem problem) {
    final Stream<String> parts =
        Stream.concat(
                Stream.of(problem.getCode(), problem.getTitle(), problem.getDetail()),
                problem.getParameters().entrySet().stream().map(Map.Entry::toString))
            .filter(Objects::nonNull);

    return problem.getCode() + "{" + parts.collect(joining(", ")) + "}";
  }

  // ------------- Builder ---------------
  interface DetailBuilder {
    CauseBuilder detail(final String detail);
  }

  interface CauseBuilder extends ParameterBuilder {
    ParameterBuilder cause(@Nullable Throwable cause);
  }

  interface ParameterBuilder extends ParametersBuilder {
    ParameterBuilder parameter(final String key, final Object value);
  }

  interface ParametersBuilder extends org.apache.commons.lang3.builder.Builder<ThrowableProblem> {
    org.apache.commons.lang3.builder.Builder<ThrowableProblem> parameters(
        @Nullable final Map<String, Object> parameters);
  }

  class ProblemBuilder implements DetailBuilder, CauseBuilder {

    private static final Set<String> RESERVED_PROPERTIES =
        new HashSet<>(Arrays.asList("code", "title", "detail", "cause"));
    private String code;
    private String title;
    private String detail;
    private ThrowableProblem cause;
    private final Map<String, Object> parameters = new LinkedHashMap<>();

    ProblemBuilder(final String code, final String title) {
      this(code, title, null);
    }

    ProblemBuilder(final String code, final String title, final String detail) {
      Assert.hasText(code, "'code' must not be null or empty");
      Assert.hasText(title, "'title' must not be null or empty");
      this.code = code;
      this.title = title;
      this.detail = detail;
    }

    @Override
    public CauseBuilder detail(final String detail) {
      this.detail = detail;
      return this;
    }

    @Override
    public ParameterBuilder cause(@Nullable final Throwable cause) {
      this.cause = Objects.nonNull(cause) ? ProblemUtils.toProblem(cause) : null;
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
        parameters.entrySet().stream()
            .forEach(entry -> parameter(entry.getKey(), entry.getValue()));
      }
      return this;
    }

    @Override
    public ThrowableProblem build() {
      return new DefaultProblem(this.code, this.title, this.detail, this.cause, this.parameters);
    }
  }
}
