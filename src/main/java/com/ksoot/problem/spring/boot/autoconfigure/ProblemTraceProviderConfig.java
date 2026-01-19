package com.ksoot.problem.spring.boot.autoconfigure;

import com.ksoot.problem.spring.config.ProblemProperties;
import io.micrometer.tracing.Tracer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnMissingBean(value = TraceProvider.class)
public class ProblemTraceProviderConfig {

  @Bean
  TraceProvider traceProvider(
      final ProblemProperties problemProperties, final ObjectProvider<Tracer> tracerProvider) {
    return new ProblemTraceProvider(problemProperties, tracerProvider);
  }
}
