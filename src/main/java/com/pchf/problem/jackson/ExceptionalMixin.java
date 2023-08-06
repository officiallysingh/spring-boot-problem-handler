package com.pchf.problem.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pchf.problem.core.ThrowableProblem;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalMixin {

  @JsonIgnore
  String getLocalizedMessage();

  @JsonIgnore
  StackTraceElement[] getStackTrace();

  @JsonIgnore
  Throwable[] getSuppressed();
}
