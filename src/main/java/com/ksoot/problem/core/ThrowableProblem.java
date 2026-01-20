package com.ksoot.problem.core;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import jakarta.annotation.Nullable;
import java.io.Serial;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Base class for all problem types that are also {@link RuntimeException}.
 *
 * <p>{@link Problem} instances are required to be immutable.
 *
 * @see Problem
 * @see Exceptional
 */
public abstract class ThrowableProblem extends RuntimeException implements Problem, Exceptional {

  @Serial private static final long serialVersionUID = 2893667887362253159L;

  /** Constructs a new throwable problem with no cause. */
  protected ThrowableProblem() {
    this(null);
  }

  /**
   * Constructs a new throwable problem with the given cause.
   *
   * @param cause the cause
   */
  protected ThrowableProblem(@Nullable final ThrowableProblem cause) {
    super(cause);
    final Collection<StackTraceElement> stackTrace =
        StackTraceProcessor.COMPOUND.process(asList(getStackTrace()));
    setStackTrace(stackTrace.toArray(new StackTraceElement[0]));
  }

  /** {@inheritDoc} */
  @Override
  public String getDetail() {
    return Stream.of(getCode(), getTitle()).filter(Objects::nonNull).collect(joining(": "));
  }

  /** {@inheritDoc} */
  @Override
  public ThrowableProblem getCause() {
    // cast is safe, since the only way to set this is our constructor
    return (ThrowableProblem) super.getCause();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return Problem.toString(this);
  }
}
