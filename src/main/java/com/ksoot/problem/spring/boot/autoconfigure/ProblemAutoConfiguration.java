package com.ksoot.problem.spring.boot.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import com.ksoot.problem.jackson.ProblemModule;
import com.ksoot.problem.spring.config.ProblemProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class ProblemAutoConfiguration {

  @Bean
  @ConditionalOnClass(value = {Module.class})
  ProblemModule problemModule() {
    return new ProblemModule();
  }
}
