package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import com.ksoot.problem.spring.boot.autoconfigure.TracingHeaderEnabledCondition;
import com.ksoot.problem.spring.config.ProblemProperties;
import io.micrometer.tracing.Tracer;
import org.jspecify.annotations.Nullable;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for
 * Problem handling in Servlet Web applications.
 */
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(
    prefix = "problem",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ProblemWebAutoConfiguration {

  /**
   * Creates the {@link ErrorResponseBuilder} bean.
   *
   * @param traceProvider the trace provider
   * @return the error response builder
   */
  @Bean
  @ConditionalOnMissingBean(ErrorResponseBuilder.class)
  ErrorResponseBuilder<NativeWebRequest, ResponseEntity<ProblemDetail>> errorResponseBuilder(
      @Nullable final TraceProvider traceProvider) {
    return new SpringWebErrorResponseBuilder(traceProvider);
  }

  /** Configuration for {@link ProblemTracingWebFilter} in Servlet web applications. */
  @Conditional(TracingHeaderEnabledCondition.class)
  @ConditionalOnClass(Tracer.class)
  @ConditionalOnMissingBean(name = "problemTracingFilter")
  static class ProblemTracingWebFilterConfiguration {

    /**
     * Creates the {@link ProblemTracingWebFilter} bean.
     *
     * @param traceProvider the trace provider
     * @return the problem tracing filter
     */
    @Bean
    ProblemTracingWebFilter problemTracingFilter(final TraceProvider traceProvider) {
      return new ProblemTracingWebFilter(traceProvider);
    }
  }
}
