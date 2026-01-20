package com.ksoot.problem.spring.config;

/**
 * A configuration related runtime exception.
 *
 * @author Rajveer Singh
 */
public class ProblemConfigException extends RuntimeException {
  /** The serial version ID. */
  private static final long serialVersionUID = -7838702245512140996L;

  /** Constructs a new {@code ProblemConfigException} without specified detail message. */
  public ProblemConfigException() {
    super();
  }

  /**
   * Constructs a new {@code ProblemConfigException} with specified detail message.
   *
   * @param message the error message
   */
  public ProblemConfigException(final String message) {
    super(message);
  }

  /**
   * Constructs a new {@code ProblemConfigException} with specified detail message using {@link
   * String#format(String, Object...)}.
   *
   * @param message the error message
   * @param args arguments to the error message
   * @see String#format(String, Object...)
   */
  public ProblemConfigException(final String message, final Object... args) {
    super(String.format(message, args));
  }

  /**
   * Constructs a new {@code ProblemConfigException} with specified nested {@code Throwable}.
   *
   * @param cause the exception or error that caused this exception to be thrown
   */
  public ProblemConfigException(final Throwable cause) {
    super(cause);
  }

  /**
   * Constructs a new {@code ProblemConfigException} with specified detail message and nested {@code
   * Throwable}.
   *
   * @param message the error message
   * @param cause the exception or error that caused this exception to be thrown
   */
  public ProblemConfigException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
