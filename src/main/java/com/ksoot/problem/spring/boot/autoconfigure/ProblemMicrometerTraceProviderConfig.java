package com.ksoot.problem.spring.boot.autoconfigure;

import com.ksoot.problem.spring.config.ProblemProperties;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configuration for {@link TraceProvider}. */
@Configuration
@ConditionalOnClass(Tracer.class)
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnMissingBean(value = TraceProvider.class)
public class ProblemMicrometerTraceProviderConfig {

  /**
   * Creates a {@link ProblemMicrometerTraceProvider} bean.
   *
   * @param problemProperties the problem properties
   * @param tracerProvider the micrometer tracer provider
   * @return the trace provider
   */
  @Bean
  TraceProvider traceProvider(
      final ProblemProperties problemProperties, final ObjectProvider<Tracer> tracerProvider) {
    return new ProblemMicrometerTraceProvider(problemProperties, tracerProvider);
  }
}
