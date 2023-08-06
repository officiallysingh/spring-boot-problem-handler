package com.ksoot.problem.spring.boot.autoconfigure;

import com.fasterxml.jackson.databind.Module;
import com.ksoot.problem.jackson.ProblemModule;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(ProblemJacksonEnabled.class)
@ConditionalOnWebApplication
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@Order(value = Ordered.HIGHEST_PRECEDENCE)
class ProblemJacksonConfiguration {

  @ConditionalOnClass(value = {ProblemModule.class, Module.class})
  @ConditionalOnMissingBean(value = ProblemModule.class)
  public static class ProblemModuleConfiguration {

    @Bean
    @ConditionalOnMissingBean
    ProblemModule problemModule(final ProblemProperties problemProperties) {
      return new ProblemModule().with(problemProperties.isStacktraceEnabled(), problemProperties.isCauseChainsEnabled());
    }
  }
}
