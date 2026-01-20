package com.ksoot.problem.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class that registers a {@link ProblemMessageProvider} bean.
 *
 * @author Rajveer Singh
 */
@Configuration
@ConditionalOnMissingBean(value = ProblemMessageProvider.class)
public class ProblemMessageProviderConfig {

  /**
   * Creates a {@link ProblemMessageProvider} bean if one doesn't already exist.
   *
   * @param messageSource the {@link MessageSource} to be used by the provider
   * @return the {@link ProblemMessageProvider} instance
   */
  @Bean
  ProblemMessageProvider problemMessageProvider(final MessageSource messageSource) {
    return new ProblemMessageProvider(messageSource);
  }
}
