package com.ksoot.problem.spring.boot.autoconfigure;

import com.ksoot.problem.spring.config.ProblemProperties;
import io.micrometer.tracing.Tracer;
import jakarta.validation.constraints.NotEmpty;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.ObjectProvider;

@RequiredArgsConstructor
public class ProblemTraceProvider implements TraceProvider {

  private final ProblemProperties problemProperties;

  private final ObjectProvider<Tracer> tracerProvider;

  @Override
  public ImmutablePair<@NotEmpty String, String> getTraceId() {
    Tracer tracer = this.tracerProvider.getIfAvailable(() -> null);
    if (Objects.nonNull(tracer) && Objects.nonNull(tracer.currentSpan().context())) {
      String traceId =
          Objects.nonNull(tracer.currentTraceContext().context())
              ? tracer.currentTraceContext().context().traceId()
              : null;
      return ImmutablePair.of(this.problemProperties.getTracing().getTraceId(), traceId);
    } else {
      return null;
    }
  }
}
