package com.pchf.problem.spring.boot.autoconfigure.web;

import com.pchf.problem.ProblemDetails;
import com.pchf.problem.spring.advice.web.ProblemHandlingWeb;
import com.pchf.problem.spring.boot.autoconfigure.ProblemProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ControllerAdvice
class ExceptionHandler implements ProblemHandlingWeb<ResponseEntity<ProblemDetails>> {

}