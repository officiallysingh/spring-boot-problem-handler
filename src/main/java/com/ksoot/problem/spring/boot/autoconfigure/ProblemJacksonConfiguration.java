package com.ksoot.problem.spring.boot.autoconfigure;

import com.ksoot.problem.jackson.ProblemModule;
import com.ksoot.problem.spring.config.ProblemProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(ProblemJacksonEnabled.class)
//@ConditionalOnWebApplication
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class ProblemJacksonConfiguration {

  @Bean
  ProblemModule problemModule() {
    return new ProblemModule();
  }
}
