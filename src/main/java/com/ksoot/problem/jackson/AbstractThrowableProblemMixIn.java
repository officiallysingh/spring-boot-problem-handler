package com.ksoot.problem.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ksoot.problem.core.AbstractThrowableProblem;
import com.ksoot.problem.core.ThrowableProblem;

abstract class AbstractThrowableProblemMixIn {

  @SuppressWarnings("serial")
  @JsonCreator
  AbstractThrowableProblemMixIn(
      @JsonProperty("code") final String code,
      @JsonProperty("title") final String title,
      @JsonProperty("detail") final String detail,
      @JsonProperty("cause") final ThrowableProblem cause) {
    // this is just here to see whether "our" constructor matches the real one
    throw new AbstractThrowableProblem(code, title, detail, cause) {};
  }

  @JsonAnySetter
  abstract void set(final String key, final Object value);
}
