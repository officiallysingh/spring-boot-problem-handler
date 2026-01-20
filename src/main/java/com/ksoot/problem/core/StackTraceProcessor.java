package com.ksoot.problem.core;

import static java.util.ServiceLoader.load;
import static java.util.stream.StreamSupport.stream;

import java.util.Collection;

/**
 * Interface for processing stack trace elements.
 *
 * @see java.util.ServiceLoader
 */
public interface StackTraceProcessor {

  /** Default stack trace processor that returns elements as is. */
  StackTraceProcessor DEFAULT = elements -> elements;

  /**
   * Compound stack trace processor that loads all available {@link StackTraceProcessor}s using
   * {@link java.util.ServiceLoader}.
   */
  StackTraceProcessor COMPOUND =
      stream(load(StackTraceProcessor.class).spliterator(), false)
          .reduce(DEFAULT, (first, second) -> elements -> second.process(first.process(elements)));

  /**
   * Processes the given collection of stack trace elements.
   *
   * @param elements the elements to process
   * @return the processed elements
   */
  Collection<StackTraceElement> process(final Collection<StackTraceElement> elements);
}
