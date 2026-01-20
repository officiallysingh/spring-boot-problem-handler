package com.ksoot.problem.core;

import jakarta.annotation.Nullable;
import java.util.Map;

/** Default implementation of {@link ThrowableProblem}. */
public final class DefaultProblem extends AbstractThrowableProblem {

  private static final long serialVersionUID = -6866968751952328910L;

  /**
   * Internal constructor for Jackson.
   *
   * @param code the problem code
   * @param title the problem title
   * @param detail the problem detail
   * @param cause the problem cause
   */
  DefaultProblem(
      final String code,
      final String title,
      final String detail,
      @Nullable final ThrowableProblem cause) {
    super(code, title, detail, cause);
  }

  /**
   * Constructs a new default problem with the given properties.
   *
   * @param code the problem code
   * @param title the problem title
   * @param detail the problem detail
   * @param cause the problem cause
   * @param parameters additional parameters
   */
  DefaultProblem(
      final String code,
      final String title,
      final String detail,
      @Nullable final ThrowableProblem cause,
      @Nullable final Map<String, Object> parameters) {
    super(code, title, detail, cause, parameters);
  }
}
