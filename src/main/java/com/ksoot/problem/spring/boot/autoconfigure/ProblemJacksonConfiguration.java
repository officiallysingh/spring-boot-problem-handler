package com.ksoot.problem.spring.boot.autoconfigure;

import com.ksoot.problem.jackson.ProblemModule;
import com.ksoot.problem.spring.config.ProblemProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration Auto-configuration} for
 * Jackson Problem module.
 */
@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(ProblemJacksonEnabled.class)
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class ProblemJacksonConfiguration {

  /**
   * Creates a {@link ProblemModule} bean.
   *
   * @return the problem module
   */
  @Bean
  ProblemModule problemModule() {
    return new ProblemModule();
  }
}
