package com.ksoot.problem.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(value = ProblemMessageProvider.class)
public class ProblemMessageProviderConfig {

  @Bean
  ProblemMessageProvider problemMessageProvider(final MessageSource messageSource) {
    return new ProblemMessageProvider(messageSource);
  }
}
