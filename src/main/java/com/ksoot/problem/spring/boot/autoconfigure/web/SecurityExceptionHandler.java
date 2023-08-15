package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.spring.advice.security.ProblemAccessDeniedHandler;
import com.ksoot.problem.spring.advice.security.ProblemAuthenticationEntryPoint;
import com.ksoot.problem.spring.advice.security.SecurityAdviceTraits;
import com.ksoot.problem.spring.boot.autoconfigure.SecurityAdviceEnabled;
import com.ksoot.problem.spring.config.ProblemProperties;
import lombok.RequiredArgsConstructor;
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

@Configuration
@EnableConfigurationProperties({ProblemProperties.class})
@Conditional(SecurityAdviceEnabled.class)
@ConditionalOnClass(value = {WebSecurityConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor
class SecurityExceptionHandler implements SecurityAdviceTraits<NativeWebRequest, ResponseEntity<ProblemDetail>> {

  @ConditionalOnMissingBean
  @Bean
  AuthenticationEntryPoint authenticationEntryPoint(
      final HandlerExceptionResolver resolver) {
    return new ProblemAuthenticationEntryPoint(resolver);
  }

  @ConditionalOnMissingBean
  @Bean
  AccessDeniedHandler accessDeniedHandler(
      final HandlerExceptionResolver resolver) {
    return new ProblemAccessDeniedHandler(resolver);
  }
}
