package com.pchf.problem.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pchf.problem.core.ThrowableProblem;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalWithoutStacktraceAndCauseMixin extends ExceptionalMixin {

  @Override
  @JsonIgnore
  StackTraceElement[] getStackTrace();

  @Override
  @JsonIgnore
  ThrowableProblem getCause();
}
