package com.ksoot.problem.spring.boot.autoconfigure.webflux;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksoot.problem.spring.advice.security.ProblemServerAccessDeniedHandler;
import com.ksoot.problem.spring.advice.security.ProblemServerAuthenticationEntryPoint;
import com.ksoot.problem.spring.advice.security.SecurityAdviceTraits;
import com.ksoot.problem.spring.boot.autoconfigure.SecurityAdviceEnabled;
import com.ksoot.problem.spring.config.ProblemProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@AutoConfiguration
@EnableConfigurationProperties({ProblemProperties.class})
@Conditional(SecurityAdviceEnabled.class)
@ConditionalOnClass(value = {WebSecurityConfiguration.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor
public class SecurityExceptionHandler
    implements SecurityAdviceTraits<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> {

  @ConditionalOnMissingBean
  @Bean
  ServerAuthenticationEntryPoint authenticationEntryPoint(
      final SecurityAdviceTraits<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> advice,
      final ObjectMapper objectMapper) {
    return new ProblemServerAuthenticationEntryPoint(advice, objectMapper);
  }

  @ConditionalOnMissingBean
  @Bean
  ServerAccessDeniedHandler accessDeniedHandler(
      final SecurityAdviceTraits<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> advice,
      final ObjectMapper objectMapper) {
    return new ProblemServerAccessDeniedHandler(advice, objectMapper);
  }
}
