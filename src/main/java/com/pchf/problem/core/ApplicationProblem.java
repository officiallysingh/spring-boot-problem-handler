package com.pchf.problem.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;

import java.util.Map;

@SuppressWarnings("serial")
public final class ApplicationProblem extends ThrowableProblem {

  private final HttpStatus status;

  private final String code;

  private final String title;

  private final String message;

  private final String details;

  private Map<String, Object> parameters;

  public ApplicationProblem(final HttpStatus status, final String code, final String title, final String message,
                            final String details, @Nullable final ThrowableProblem cause, @Nullable final Map<String, Object> parameters) {
    super(cause);
    Validate.notNull(status, "'status' must not be null");
    Validate.notBlank(code, "'code' must not be null or empty");
    Validate.notBlank(title, "'title' must not be null or empty");
    Validate.notBlank(message, "'message' must not be null or empty");
    Validate.notBlank(details, "'details' must not be null or empty");
    this.status = status;
    this.code = code;
    this.title = title;
    this.message = message;
    this.details = details;
    this.parameters = parameters;
  }

  public static ApplicationProblem of(final HttpStatus status, final String code, final String title,
                                      final String message, final String details) {
    return new ApplicationProblem(status, code, title, message, details, null, null);
  }

  public static ApplicationProblem of(final HttpStatus status, final String code, final String title,
                                      final String message, final String details, @Nullable final ThrowableProblem cause) {
    return new ApplicationProblem(status, code, title, message, details, cause, null);
  }

  public static ApplicationProblem of(final HttpStatus status, final String code, final String title,
                                      final String message, final String details, @Nullable final Map<String, Object> parameters) {
    return new ApplicationProblem(status, code, title, message, details, null, parameters);
  }

  public static ApplicationProblem of(final HttpStatus status, final String code, final String title,
                                      final String message, final String details, @Nullable final ThrowableProblem cause,
                                      @Nullable final Map<String, Object> parameters) {
    return new ApplicationProblem(status, code, title, message, details, cause, parameters);
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
  public String getMessage() {
    return this.message;
  }

  @Override
  public String getDetails() {
    return this.details;
  }

  @Override
  public Map<String, Object> getParameters() {
    return this.parameters;
  }
}
