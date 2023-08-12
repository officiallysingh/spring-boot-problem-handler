package com.ksoot.problem.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingBean(value = ProblemMessageProvider.class)
public class ProblemMessageProviderConfig {

  @Bean
  public ProblemMessageProvider problemMessageProvider(final MessageSource messageSource) {
    return new ProblemMessageProvider(messageSource);
  }
}
