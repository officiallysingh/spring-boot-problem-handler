package com.ksoot.problem.spring.boot.autoconfigure;

import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.tuple.ImmutablePair;

public interface TraceProvider {

  ImmutablePair<@NotEmpty String, String> getTraceId();
}
