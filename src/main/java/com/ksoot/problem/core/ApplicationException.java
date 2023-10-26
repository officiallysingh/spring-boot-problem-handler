package com.ksoot.problem.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

@SuppressWarnings("serial")
public final class ApplicationException extends Exception implements Exceptional {

  private final HttpStatus status;

  private final String code;

  private final String title;

  private final String detail;

  private Map<String, Object> parameters;

  private ThrowableProblem cause;

  public ApplicationException(
      final HttpStatus status,
      final String code,
      final String title,
      final String detail,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    super(cause);
    Assert.notNull(status, "'status' must not be null");
    Assert.hasText(code, "'code' must not be null or empty");
    Assert.hasText(title, "'title' must not be null or empty");
    Assert.hasText(detail, "'detail' must not be null or empty");
    this.status = status;
    this.code = code;
    this.title = title;
    this.detail = detail;
    this.cause = cause;
    this.parameters = parameters;
  }

  public static ApplicationException of(
      final HttpStatus status, final String code, final String title, final String detail) {
    return new ApplicationException(status, code, title, detail, null, null);
  }

  public static ApplicationException of(
      final HttpStatus status,
      final String code,
      final String title,
      final String detail,
      @Nullable final ThrowableProblem cause) {
    return new ApplicationException(status, code, title, detail, cause, null);
  }

  public static ApplicationException of(
      final HttpStatus status,
      final String code,
      final String title,
      final String detail,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    return new ApplicationException(status, code, title, detail, cause, parameters);
  }

  @JsonIgnore
  public HttpStatus status() {
    return this.status;
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
  public String getDetail() {
    return this.detail;
  }

  @Override
  public Map<String, Object> getParameters() {
    return this.parameters;
  }

  @Override
  public ThrowableProblem getCause() {
    return this.cause;
  }
}
