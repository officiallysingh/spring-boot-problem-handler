package com.pchf.problem.spring.boot.autoconfigure.web;

import com.pchf.problem.ProblemDetails;
import com.pchf.problem.core.ErrorResponseBuilder;
import com.pchf.problem.spring.boot.autoconfigure.ProblemProperties;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Registers Problem Jackson modules when {@link WebMvcAutoConfiguration} is
 * enabled.
 */
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Configuration(proxyBeanMethods = false)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class ProblemWebAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  ErrorResponseBuilder<NativeWebRequest, ResponseEntity<ProblemDetails>> errorResponseBuilder() {
    return new SpringWebErrorResponseBuilder();
  }
}
