package com.pchf.problem.core;

import jakarta.annotation.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractThrowableProblem extends ThrowableProblem {

  private static final long serialVersionUID = 7657146691407810390L;

  private final String code;
  private final String title;
  private final String message;
  private final String details;
  private final Map<String, Object> parameters;

  protected AbstractThrowableProblem() {
    this(null, null);
  }

  protected AbstractThrowableProblem(final String code, final String title) {
    this(code, title, null);
  }

  protected AbstractThrowableProblem(final String code, final String title,
                                     final String message) {
    this(code, title, message, null);
  }

  protected AbstractThrowableProblem(final String code, final String title,
                                     final String message, final String details) {
    this(code, title, message, details, null);
  }

  protected AbstractThrowableProblem(final String code, final String title,
                                     final String message, final String details, @Nullable final ThrowableProblem cause) {
    this(code, title, message, details, cause, null);
  }

  protected AbstractThrowableProblem(final String code, final String title,
                                     final String message, final String details, @Nullable final ThrowableProblem cause,
                                     @Nullable final Map<String, Object> parameters) {
    super(cause);
    this.code = code;
    this.title = title;
    this.message = message;
    this.details = details;
    this.parameters = Optional.ofNullable(parameters).orElseGet(LinkedHashMap::new);
  }

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public String getMessage() {
    return this.message;
  }

  @Override
  public String getDetails() {
    return this.details;
  }

  @Override
  public Map<String, Object> getParameters() {
    return Collections.unmodifiableMap(this.parameters);
  }

  /**
   * This is required to workaround missing support for
   * {@link com.fasterxml.jackson.annotation.JsonAnySetter} on constructors
   * annotated with {@link com.fasterxml.jackson.annotation.JsonCreator}.
   *
   * @param key   the custom key
   * @param value the custom value
   * @see <a href=
   * "https://github.com/FasterXML/jackson-databind/issues/562">Jackson Issue
   * 562</a>
   */
  void set(final String key, final Object value) {
    this.parameters.put(key, value);
  }
}
