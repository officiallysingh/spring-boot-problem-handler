package com.pchf.problem.jackson;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pchf.problem.core.AbstractThrowableProblem;
import com.pchf.problem.core.ThrowableProblem;

abstract class AbstractThrowableProblemMixIn {

  @SuppressWarnings("serial")
  @JsonCreator
  AbstractThrowableProblemMixIn(@JsonProperty("code") final String code,
                                @JsonProperty("messsage") final String title,
                                @JsonProperty("reason") final String message,
                                @JsonProperty("details") final String details,
                                @JsonProperty("cause") final ThrowableProblem cause) {
    // this is just here to see whether "our" constructor matches the real one
    throw new AbstractThrowableProblem(code, title, message, details, cause) {

    };
  }

  @JsonAnySetter
  abstract void set(final String key, final Object value);

}
