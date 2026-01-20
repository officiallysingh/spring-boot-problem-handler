package com.ksoot.problem.spring.config;

import com.ksoot.problem.core.ErrorResponseBuilder;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * Helper class that provides static access to the Spring {@link ApplicationContext} and common
 * beans. This class implements {@link ApplicationContextAware} to capture the context on startup.
 *
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
public class ProblemBeanRegistry implements ApplicationContextAware {

  private static ApplicationContext applicationContext;

  /**
   * Provides the Spring {@link ApplicationContext}.
   *
   * @return the {@link ApplicationContext} instance
   */
  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  /** {@inheritDoc} */
  @Override
  @Autowired
  public void setApplicationContext(@NonNull ApplicationContext applicationContext)
      throws BeansException {
    ProblemBeanRegistry.applicationContext = applicationContext;
  }

  /**
   * Retrieves a bean of the specified type from the application context.
   *
   * @param <T> the type of the bean
   * @param requiredType the class of the bean to retrieve
   * @return the bean instance
   */
  public static <T> T getBean(Class<T> requiredType) {
    return applicationContext.getBean(requiredType);
  }

  /**
   * Retrieves a bean by name and type from the application context.
   *
   * @param <T> the type of the bean
   * @param name the name of the bean
   * @param requiredType the class of the bean to retrieve
   * @return the bean instance
   */
  public static <T> T getBean(String name, Class<T> requiredType) {
    return applicationContext.getBean(name, requiredType);
  }

  /**
   * Retrieves the {@link ProblemProperties} bean from the application context.
   *
   * @return the {@link ProblemProperties} instance
   */
  public static ProblemProperties problemProperties() {
    return applicationContext.getBean(ProblemProperties.class);
  }

  /**
   * Retrieves the {@link ErrorResponseBuilder} bean from the application context.
   *
   * @param <T> the type of the error response
   * @param <R> the type of the response entity
   * @return the {@link ErrorResponseBuilder} instance
   */
  @SuppressWarnings("unchecked")
  public static <T, R> ErrorResponseBuilder<T, R> errorResponseBuilder() {
    return applicationContext.getBean(ErrorResponseBuilder.class);
  }
}
