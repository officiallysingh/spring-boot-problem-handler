package com.pchf.problem.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pchf.problem.core.ThrowableProblem;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalWithoutCauseMixin extends ExceptionalMixin {

  @Override
  @JsonIgnore
  ThrowableProblem getCause();
}
