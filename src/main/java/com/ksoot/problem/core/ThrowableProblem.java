package com.ksoot.problem.core;

import jakarta.annotation.Nullable;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

/**
 * {@link Problem} instances are required to be immutable.
 */
public abstract class ThrowableProblem extends RuntimeException implements Problem, Exceptional {

  private static final long serialVersionUID = 2893667887362253159L;

  protected ThrowableProblem() {
    this(null);
  }

  protected ThrowableProblem(@Nullable final ThrowableProblem cause) {
    super(cause);

    final Collection<StackTraceElement> stackTrace = StackTraceProcessor.COMPOUND.process(asList(getStackTrace()));
    setStackTrace(stackTrace.toArray(new StackTraceElement[0]));
  }

  @Override
  public String getDetail() {
    return Stream.of(getCode(), getTitle())
        .filter(Objects::nonNull)
        .collect(joining(": "));
  }

  @Override
  public ThrowableProblem getCause() {
    // cast is safe, since the only way to set this is our constructor
    return (ThrowableProblem) super.getCause();
  }

  @Override
  public String toString() {
    return Problem.toString(this);
  }
}
