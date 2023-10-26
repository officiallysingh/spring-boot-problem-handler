package com.ksoot.problem.spring.config;

import com.ksoot.problem.core.ProblemConstant;
import jakarta.validation.ConstraintViolation;
import java.io.Serializable;
import java.util.Arrays;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * @author Rajveer Singh
 */
@SuppressWarnings("serial")
public class ProblemMessageSourceResolver implements MessageSourceResolvable, Serializable {

  private final String[] codes;

  @Nullable private String defaultMessage;

  @Nullable private Object[] arguments;

  private ProblemMessageSourceResolver(
      final String[] codes, final String defaultMessage, Object[] arguments) {
    this.codes = codes;
    this.defaultMessage = defaultMessage;
    this.arguments = arguments;
  }

  public static ProblemMessageSourceResolver of(final String code) {
    return new ProblemMessageSourceResolver(new String[] {code}, null, null);
  }

  public static ProblemMessageSourceResolver of(final String[] codes) {
    return new ProblemMessageSourceResolver(codes, null, null);
  }

  public static ProblemMessageSourceResolver of(final String code, final Object[] arguments) {
    return new ProblemMessageSourceResolver(new String[] {code}, null, arguments);
  }

  public static ProblemMessageSourceResolver of(final String[] codes, final Object[] arguments) {
    return new ProblemMessageSourceResolver(codes, null, arguments);
  }

  public static ProblemMessageSourceResolver of(final String[] codes, final String defaultMessage) {
    return new ProblemMessageSourceResolver(codes, defaultMessage, null);
  }

  public static ProblemMessageSourceResolver of(final String[] codes, final int status) {
    return new ProblemMessageSourceResolver(codes, "" + status, null);
  }

  public static ProblemMessageSourceResolver of(final String code, final String defaultMessage) {
    return new ProblemMessageSourceResolver(new String[] {code}, defaultMessage, null);
  }

  public static ProblemMessageSourceResolver of(final String code, final int statusCode) {
    return new ProblemMessageSourceResolver(new String[] {code}, "" + statusCode, null);
  }

  public static ProblemMessageSourceResolver of(
      final String[] codes, final String defaultMessage, final Object[] arguments) {
    return new ProblemMessageSourceResolver(codes, defaultMessage, arguments);
  }

  public static ProblemMessageSourceResolver of(
      final String code, final String defaultMessage, final Object[] arguments) {
    return new ProblemMessageSourceResolver(new String[] {code}, defaultMessage, arguments);
  }

  public static ProblemMessageSourceResolver of(
      final String prefix, final ObjectError objectError, final String defaultMessage) {
    return of(
        Arrays.stream(objectError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        defaultMessage);
  }

  public static ProblemMessageSourceResolver of(
      final String prefix, final ObjectError objectError, final int status) {
    return of(
        Arrays.stream(objectError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        "" + status);
  }

  public static ProblemMessageSourceResolver of(
      final String prefix, final ObjectError objectError) {
    return new ProblemMessageSourceResolver(
        Arrays.stream(objectError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        objectError.getDefaultMessage(),
        null);
  }

  public static ProblemMessageSourceResolver of(final String prefix, final FieldError fieldError) {
    return new ProblemMessageSourceResolver(
        Arrays.stream(fieldError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        fieldError.getDefaultMessage(),
        null);
  }

  public static ProblemMessageSourceResolver of(
      final String prefix, final FieldError fieldError, final String defaultMessage) {
    return of(
        Arrays.stream(fieldError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        defaultMessage);
  }

  public static ProblemMessageSourceResolver of(
      final String prefix, final FieldError fieldError, final int status) {
    return of(
        Arrays.stream(fieldError.getCodes())
            .map(code -> prefix + ProblemConstant.DOT + code)
            .toArray(String[]::new),
        "" + status);
  }

  @SuppressWarnings("rawtypes")
  public static ProblemMessageSourceResolver of(
      final String prefix, final ConstraintViolation violation) {
    return of(
        prefix + ProblemConstant.DOT + violation.getPropertyPath().toString(),
        violation.getMessage());
  }

  @SuppressWarnings("rawtypes")
  public static ProblemMessageSourceResolver of(
      final String prefix, final ConstraintViolation violation, final String defaultMessage) {
    return of(
        prefix + ProblemConstant.DOT + violation.getPropertyPath().toString(), defaultMessage);
  }

  @SuppressWarnings("rawtypes")
  public static ProblemMessageSourceResolver of(
      final String prefix, final ConstraintViolation violation, final int status) {
    return of(prefix + ProblemConstant.DOT + violation.getPropertyPath().toString(), "" + status);
  }

  @Override
  public String[] getCodes() {
    return this.codes;
  }

  @Override
  public String getDefaultMessage() {
    return this.defaultMessage;
  }

  @Override
  public Object[] getArguments() {
    return this.arguments;
  }
}
