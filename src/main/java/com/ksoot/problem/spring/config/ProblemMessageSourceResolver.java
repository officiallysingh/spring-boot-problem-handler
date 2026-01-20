package com.ksoot.problem.spring.config;

import com.ksoot.problem.core.ProblemConstant;
import jakarta.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Arrays;
import org.jspecify.annotations.Nullable;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * Implementation of {@link MessageSourceResolvable} for resolving problem-related messages. This
 * class provides various factory methods to create instances from different sources like error
 * codes, {@link ObjectError}, {@link FieldError}, or {@link ConstraintViolation}.
 *
 * @author Rajveer Singh
 */
@SuppressWarnings("serial")
public class ProblemMessageSourceResolver implements MessageSourceResolvable, Serializable {

  private final String[] codes;

  @Nullable private final String defaultMessage;

  @Nullable private final Object[] arguments;

  private ProblemMessageSourceResolver(
      final String[] codes, final @Nullable String defaultMessage, Object[] arguments) {
    this.codes = codes;
    this.defaultMessage = defaultMessage;
    this.arguments = arguments;
  }

  /**
   * Creates a resolver for a single error code.
   *
   * @param code the error code
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String code) {
    return new ProblemMessageSourceResolver(new String[] {code}, null, null);
  }

  /**
   * Creates a resolver for multiple error codes.
   *
   * @param codes the error codes
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String[] codes) {
    return new ProblemMessageSourceResolver(codes, null, null);
  }

  /**
   * Creates a resolver for a single error code and arguments.
   *
   * @param code the error code
   * @param arguments the message arguments
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String code, final Object[] arguments) {
    return new ProblemMessageSourceResolver(new String[] {code}, null, arguments);
  }

  /**
   * Creates a resolver for multiple error codes and arguments.
   *
   * @param codes the error codes
   * @param arguments the message arguments
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String[] codes, final Object[] arguments) {
    return new ProblemMessageSourceResolver(codes, null, arguments);
  }

  /**
   * Creates a resolver for multiple error codes and a default message.
   *
   * @param codes the error codes
   * @param defaultMessage the default message
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String[] codes, final String defaultMessage) {
    return new ProblemMessageSourceResolver(codes, defaultMessage, null);
  }

  /**
   * Creates a resolver for multiple error codes and an HTTP status as default message.
   *
   * @param codes the error codes
   * @param status the HTTP status
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String[] codes, final int status) {
    return new ProblemMessageSourceResolver(codes, String.valueOf(status), null);
  }

  /**
   * Creates a resolver for a single error code and a default message.
   *
   * @param code the error code
   * @param defaultMessage the default message
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String code, final String defaultMessage) {
    return new ProblemMessageSourceResolver(new String[] {code}, defaultMessage, null);
  }

  /**
   * Creates a resolver for a single error code and an HTTP status code as default message.
   *
   * @param code the error code
   * @param statusCode the HTTP status code
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String code, final int statusCode) {
    return new ProblemMessageSourceResolver(new String[] {code}, String.valueOf(statusCode), null);
  }

  /**
   * Creates a resolver for multiple error codes, a default message, and arguments.
   *
   * @param codes the error codes
   * @param defaultMessage the default message
   * @param arguments the message arguments
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(
      final String[] codes, final String defaultMessage, final Object[] arguments) {
    return new ProblemMessageSourceResolver(codes, defaultMessage, arguments);
  }

  /**
   * Creates a resolver for a single error code, a default message, and arguments.
   *
   * @param code the error code
   * @param defaultMessage the default message
   * @param arguments the message arguments
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(
      final String code, final String defaultMessage, final Object[] arguments) {
    return new ProblemMessageSourceResolver(new String[] {code}, defaultMessage, arguments);
  }

  /**
   * Creates a resolver from an {@link ObjectError} with a prefix.
   *
   * @param prefix the prefix for the error codes
   * @param objectError the object error
   * @param defaultMessage the default message
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(
      final String prefix, final ObjectError objectError, final String defaultMessage) {
    return of(
        Arrays.stream(objectError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        defaultMessage);
  }

  /**
   * Creates a resolver from an {@link ObjectError} with a prefix and HTTP status.
   *
   * @param prefix the prefix for the error codes
   * @param objectError the object error
   * @param status the HTTP status
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(
      final String prefix, final ObjectError objectError, final int status) {
    return of(
        Arrays.stream(objectError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        "" + status);
  }

  /**
   * Creates a resolver from an {@link ObjectError} with a prefix. Uses the default message from the
   * {@link ObjectError}.
   *
   * @param prefix the prefix for the error codes
   * @param objectError the object error
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(
      final String prefix, final ObjectError objectError) {
    return new ProblemMessageSourceResolver(
        Arrays.stream(objectError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        objectError.getDefaultMessage(),
        null);
  }

  /**
   * Creates a resolver from a {@link FieldError} with a prefix. Uses the default message from the
   * {@link FieldError}.
   *
   * @param prefix the prefix for the error codes
   * @param fieldError the field error
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(final String prefix, final FieldError fieldError) {
    return new ProblemMessageSourceResolver(
        Arrays.stream(fieldError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        fieldError.getDefaultMessage(),
        null);
  }

  /**
   * Creates a resolver from a {@link FieldError} with a prefix and default message.
   *
   * @param prefix the prefix for the error codes
   * @param fieldError the field error
   * @param defaultMessage the default message
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(
      final String prefix, final FieldError fieldError, final String defaultMessage) {
    return of(
        Arrays.stream(fieldError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        defaultMessage);
  }

  /**
   * Creates a resolver from a {@link FieldError} with a prefix and HTTP status.
   *
   * @param prefix the prefix for the error codes
   * @param fieldError the field error
   * @param status the HTTP status
   * @return a new {@code ProblemMessageSourceResolver}
   */
  public static ProblemMessageSourceResolver of(
      final String prefix, final FieldError fieldError, final int status) {
    return of(
        Arrays.stream(fieldError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        "" + status);
  }

  /**
   * Creates a resolver from a {@link ConstraintViolation} with a prefix. Uses the message from the
   * {@link ConstraintViolation} as default.
   *
   * @param prefix the prefix for the error code
   * @param violation the constraint violation
   * @return a new {@code ProblemMessageSourceResolver}
   */
  @SuppressWarnings("rawtypes")
  public static ProblemMessageSourceResolver of(
      final String prefix, final ConstraintViolation violation) {
    return of(
        prefix + ProblemConstant.DOT + violation.getPropertyPath().toString(),
        violation.getMessage());
  }

  /**
   * Creates a resolver from a {@link ConstraintViolation} with a prefix and default message.
   *
   * @param prefix the prefix for the error code
   * @param violation the constraint violation
   * @param defaultMessage the default message
   * @return a new {@code ProblemMessageSourceResolver}
   */
  @SuppressWarnings("rawtypes")
  public static ProblemMessageSourceResolver of(
      final String prefix, final ConstraintViolation violation, final String defaultMessage) {
    return of(
        prefix + ProblemConstant.DOT + violation.getPropertyPath().toString(), defaultMessage);
  }

  /**
   * Creates a resolver from a {@link ConstraintViolation} with a prefix and HTTP status.
   *
   * @param prefix the prefix for the error code
   * @param violation the constraint violation
   * @param status the HTTP status
   * @return a new {@code ProblemMessageSourceResolver}
   */
  @SuppressWarnings("rawtypes")
  public static ProblemMessageSourceResolver of(
      final String prefix, final ConstraintViolation violation, final int status) {
    return of(prefix + ProblemConstant.DOT + violation.getPropertyPath().toString(), "" + status);
  }

  /** {@inheritDoc} */
  @Override
  public String[] getCodes() {
    return this.codes;
  }

  /** {@inheritDoc} */
  @Override
  public @Nullable String getDefaultMessage() {
    return this.defaultMessage;
  }

  /** {@inheritDoc} */
  @Override
  public Object[] getArguments() {
    return this.arguments;
  }
}
