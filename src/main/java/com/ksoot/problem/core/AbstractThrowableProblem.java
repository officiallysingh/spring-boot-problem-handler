package com.ksoot.problem.core;

import jakarta.annotation.Nullable;
import java.io.Serial;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Base class for all {@link ThrowableProblem} implementations that provides basic property support.
 */
public abstract class AbstractThrowableProblem extends ThrowableProblem {

  @Serial private static final long serialVersionUID = 7657146691407810390L;

  private final String code;
  private final String title;
  private final String detail;
  private final Map<String, Object> parameters;

  /** Constructs a new abstract throwable problem with no properties. */
  protected AbstractThrowableProblem() {
    this(null, null);
  }

  /**
   * Constructs a new abstract throwable problem with the given code and title.
   *
   * @param code the problem code
   * @param title the problem title
   */
  protected AbstractThrowableProblem(final String code, final String title) {
    this(code, title, null);
  }

  /**
   * Constructs a new abstract throwable problem with the given code, title, and detail.
   *
   * @param code the problem code
   * @param title the problem title
   * @param detail the problem detail
   */
  protected AbstractThrowableProblem(final String code, final String title, final String detail) {
    this(code, title, detail, null);
  }

  /**
   * Constructs a new abstract throwable problem with the given code, title, detail, and cause.
   *
   * @param code the problem code
   * @param title the problem title
   * @param detail the problem detail
   * @param cause the problem cause
   */
  protected AbstractThrowableProblem(
      final String code,
      final String title,
      final String detail,
      @Nullable final ThrowableProblem cause) {
    this(code, title, detail, cause, null);
  }

  /**
   * Constructs a new abstract throwable problem with the given properties.
   *
   * @param code the problem code
   * @param title the problem title
   * @param detail the problem detail
   * @param cause the problem cause
   * @param parameters additional parameters
   */
  protected AbstractThrowableProblem(
      final String code,
      final String title,
      final String detail,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    super(cause);
    this.code = code;
    this.title = title;
    this.detail = detail;
    this.parameters = Optional.ofNullable(parameters).orElseGet(LinkedHashMap::new);
  }

  /** {@inheritDoc} */
  @Override
  public String getCode() {
    return this.code;
  }

  /** {@inheritDoc} */
  @Override
  public String getTitle() {
    return this.title;
  }

  /** {@inheritDoc} */
  @Override
  public String getDetail() {
    return this.detail;
  }

  /** {@inheritDoc} */
  @Override
  public Map<String, Object> getParameters() {
    return Collections.unmodifiableMap(this.parameters);
  }

  /**
   * This is required to workaround missing support for {@link
   * com.fasterxml.jackson.annotation.JsonAnySetter} on constructors annotated with {@link
   * com.fasterxml.jackson.annotation.JsonCreator}.
   *
   * @param key the custom key
   * @param value the custom value
   * @see <a href= "https://github.com/FasterXML/jackson-databind/issues/562">Jackson Issue 562</a>
   */
  void set(final String key, final Object value) {
    this.parameters.put(key, value);
  }
}
