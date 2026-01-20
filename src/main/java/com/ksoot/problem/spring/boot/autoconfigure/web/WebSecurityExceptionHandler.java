package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.spring.advice.security.ProblemAccessDeniedHandler;
import com.ksoot.problem.spring.advice.security.ProblemAuthenticationEntryPoint;
import com.ksoot.problem.spring.advice.security.SecurityAdviceTraits;
import com.ksoot.problem.spring.boot.autoconfigure.SecurityAdviceEnabled;
import com.ksoot.problem.spring.config.ProblemProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;

/**
 * {@link ControllerAdvice} for handling security-related exceptions in Servlet-based web
 * applications. It also provides beans for authentication entry point and access denied handler.
 *
 * @see SecurityAdviceTraits
 */
@Configuration
@EnableConfigurationProperties({ProblemProperties.class})
@Conditional(SecurityAdviceEnabled.class)
@ConditionalOnClass(value = {WebSecurityConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor
public class WebSecurityExceptionHandler
    implements SecurityAdviceTraits<NativeWebRequest, ResponseEntity<ProblemDetail>> {

  /**
   * Creates a {@link ProblemAuthenticationEntryPoint} bean.
   *
   * @param resolver the handler exception resolver
   * @return the authentication entry point
   */
  @ConditionalOnMissingBean
  @Bean
  AuthenticationEntryPoint authenticationEntryPoint(
      @Qualifier("handlerExceptionResolver") final HandlerExceptionResolver resolver) {
    return new ProblemAuthenticationEntryPoint(resolver);
  }

  /**
   * Creates a {@link ProblemAccessDeniedHandler} bean.
   *
   * @param resolver the handler exception resolver
   * @return the access denied handler
   */
  @ConditionalOnMissingBean
  @Bean
  AccessDeniedHandler accessDeniedHandler(
      @Qualifier("handlerExceptionResolver") final HandlerExceptionResolver resolver) {
    return new ProblemAccessDeniedHandler(resolver);
  }
}
