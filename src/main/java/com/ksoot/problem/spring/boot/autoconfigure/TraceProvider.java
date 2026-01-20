package com.ksoot.problem.spring.boot.autoconfigure;

import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.tuple.ImmutablePair;

/** Interface for providing trace information. */
public interface TraceProvider {

  /**
   * Returns the trace ID information as a pair of key and value. Key is the attribute name if trace
   * id to be added in body or header name if trace id to be added as header of error response
   * respectively. Value is the trace id.
   *
   * @return the trace ID information, or {@code null} if not available
   */
  ImmutablePair<@NotEmpty String, String> getTraceId();
}
