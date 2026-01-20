package com.ksoot.problem.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Jackson MixIn for {@link com.ksoot.problem.core.Exceptional} to ignore certain properties during
 * JSON processing.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalMixin {

  /** {@inheritDoc} */
  @JsonIgnore
  String getLocalizedMessage();

  /** {@inheritDoc} */
  @JsonIgnore
  String getMessage();

  /** {@inheritDoc} */
  @JsonIgnore
  StackTraceElement[] getStackTrace();

  /** {@inheritDoc} */
  @JsonIgnore
  Throwable[] getSuppressed();
}
