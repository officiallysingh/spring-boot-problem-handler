package com.pchf.problem.core;

import jakarta.annotation.Nullable;

import java.util.Map;

public final class DefaultProblem extends AbstractThrowableProblem {

  private static final long serialVersionUID = -6866968751952328910L;

  // TODO needed for jackson
  DefaultProblem(final String code, final String title, final String message,
                 @Nullable final ThrowableProblem cause) {
    super(code, title, message, cause);
  }

  DefaultProblem(final String code, final String title, final String message,
                 @Nullable final ThrowableProblem cause, @Nullable final Map<String, Object> parameters) {
    super(code, title, message, cause, parameters);
  }
}
