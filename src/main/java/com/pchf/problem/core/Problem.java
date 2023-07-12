package com.pchf.problem.core;

import jakarta.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;

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

  static String toString(final Problem problem) {
    final Stream<String> parts = Stream
        .concat(Stream.of(problem.getCode(), problem.getTitle(), problem.getMessage(), problem.getDetails()),
            problem.getParameters().entrySet().stream().map(Map.Entry::toString))
        .filter(Objects::nonNull);

    return problem.getCode() + "{" + parts.collect(joining(", ")) + "}";
  }

  // ------------- Build Problems ---------------
  static TitleBuilder code(final String code) {
    return new Builder(code);
  }

  static MessageBuilder of(final HttpStatus status) {
    return new Builder("" + status.value(), status.getReasonPhrase());
  }

  static CauseBuilder of(final String code, final String title, final String message, final String details) {
    return new Builder(code, title, message, details);
  }

  String getCode();

  /**
   * A short, human-readable summary of the problem type. It SHOULD NOT change
   * from occurrence to occurrence of the problem, except for purposes of
   * localisation.
   *
   * @return a short, human-readable summary of this problem
   */
  String getTitle();

  String getMessage();

  /**
   * A human readable explanation specific to this occurrence of the problem.
   *
   * @return A human readable explanation of this problem
   */
  String getDetails();

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

  public interface TitleBuilder {
    MessageBuilder title(final String title);
  }

  public interface MessageBuilder {
    DetailsBuilder message(final String message);
  }

  public interface DetailsBuilder {
    CauseBuilder details(final String details);
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

  public static class Builder implements TitleBuilder, MessageBuilder, DetailsBuilder, CauseBuilder {

    private static final Set<String> RESERVED_PROPERTIES = new HashSet<>(
        Arrays.asList("code", "title", "message", "details", "cause"));
    private final Map<String, Object> parameters = new LinkedHashMap<>();
    private String code;
    private String title;
    private String message;
    private String details;
    private ThrowableProblem cause;

    Builder(final String code) {
      Validate.notBlank(code, "'code' must not be null or empty");
      this.code = code;
    }

    Builder(final String code, final String title) {
      this(code);
      Validate.notBlank(title, "'title' must not be null or empty");
      this.title = title;
    }

    Builder(final String code, final String title, final String message, final String details) {
      this(code, title);
      Validate.notBlank(code, "'message' must not be null or empty");
      Validate.notBlank(code, "'details' must not be null or empty");
      this.message = message;
      this.details = details;
    }

    Builder(final String code, final String title, final String message, final String details,
            final ThrowableProblem cause) {
      this(code, title, message, details);
      this.cause = cause;
    }

    Builder(final String code, final String title, final String message, final String details,
            final ThrowableProblem cause, final Map<String, Object> parameters) {
      this(code, title, message, details, cause);
      if (MapUtils.isNotEmpty(parameters)) {
        this.parameters.putAll(parameters);
      }
    }

    @Override
    public MessageBuilder title(final String title) {
      Validate.notBlank(title, "'title' must not be null or empty");
      this.title = title;
      return this;
    }

    @Override
    public DetailsBuilder message(final String message) {
      Validate.notBlank(code, "'message' must not be null or empty");
      this.message = message;
      return this;
    }

    @Override
    public CauseBuilder details(final String details) {
      Validate.notBlank(code, "'details' must not be null or empty");
      this.details = details;
      return this;
    }

    @Override
    public ParameterBuilder cause(@Nullable final ThrowableProblem cause) {
      this.cause = cause;
      return this;
    }

    @Override
    public ParameterBuilder parameter(final String key, final Object value) {
      Validate.notBlank(key, "'key' must not be null or empty");
      Validate.isTrue(!RESERVED_PROPERTIES.contains(key), "Property " + key + " is reserved");
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
      return new DefaultProblem(this.code, this.title, this.message, this.details, this.cause, this.parameters);
    }
  }
}
