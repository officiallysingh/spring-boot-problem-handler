package com.ksoot.problem.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
interface ExceptionalMixin {

  @JsonIgnore
  String getLocalizedMessage();

  @JsonIgnore
  StackTraceElement[] getStackTrace();

  @JsonIgnore
  Throwable[] getSuppressed();
}
