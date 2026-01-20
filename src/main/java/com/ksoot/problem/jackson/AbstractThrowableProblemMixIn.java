package com.ksoot.problem.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ksoot.problem.core.AbstractThrowableProblem;
import com.ksoot.problem.core.ThrowableProblem;

/**
 * Jackson MixIn for {@link com.ksoot.problem.core.AbstractThrowableProblem} to configure JSON
 * deserialization.
 */
abstract class AbstractThrowableProblemMixIn {

  /**
   * Constructs a new MixIn for {@link com.ksoot.problem.core.AbstractThrowableProblem}.
   *
   * @param code the problem code
   * @param title the problem title
   * @param detail the problem detail
   * @param cause the problem cause
   */
  @JsonCreator
  AbstractThrowableProblemMixIn(
      @JsonProperty("code") final String code,
      @JsonProperty("title") final String title,
      @JsonProperty("detail") final String detail,
      @JsonProperty("cause") final ThrowableProblem cause) {
    // this is just here to see whether "our" constructor matches the real one
    throw new AbstractThrowableProblem(code, title, detail, cause) {};
  }

  /**
   * Sets a custom parameter.
   *
   * @param key the parameter key
   * @param value the parameter value
   */
  @JsonAnySetter
  abstract void set(final String key, final Object value);
}
