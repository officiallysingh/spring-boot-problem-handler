package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import com.ksoot.problem.spring.boot.autoconfigure.TracingHeaderEnabledCondition;
import com.ksoot.problem.spring.config.ProblemProperties;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
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

  @Bean
  @ConditionalOnMissingBean(ErrorResponseBuilder.class)
  ErrorResponseBuilder<NativeWebRequest, ResponseEntity<ProblemDetail>> errorResponseBuilder(
      final TraceProvider traceProvider) {
    return new SpringWebErrorResponseBuilder(traceProvider);
  }

  @Conditional(TracingHeaderEnabledCondition.class)
  @ConditionalOnMissingBean(name = "problemTracingFilter")
  static class ProblemTracingWebFilterConfiguration {

    @Bean
    ProblemTracingWebFilter problemTracingFilter(final TraceProvider traceProvider) {
      return new ProblemTracingWebFilter(traceProvider);
    }
  }
}
