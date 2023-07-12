package com.pchf.problem.spring.config;

import com.pchf.problem.core.ErrorResponseBuilder;
import com.pchf.problem.spring.boot.autoconfigure.ProblemProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ProblemBeanRegistry implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  @Autowired
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ProblemBeanRegistry.applicationContext = applicationContext;
  }

  public static <T> T getBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }

  public static <T> T getBean(String name, Class<T> requiredType) {
    return applicationContext.getBean(name, requiredType);
  }

  public static ProblemProperties problemProperties() {
    return applicationContext.getBean(ProblemProperties.class);
  }

  @SuppressWarnings("unchecked")
  public static <T, R> ErrorResponseBuilder<T, R> errorResponseBuilder() {
    return applicationContext.getBean(ErrorResponseBuilder.class);
  }
}
