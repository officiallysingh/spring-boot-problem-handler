package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.springmvc.OpenApiValidationInterceptor;
import com.atlassian.oai.validator.springmvc.ValidationReportHandler;
import com.ksoot.problem.spring.advice.validation.OpenApiValidationAdviceTrait;
import com.ksoot.problem.spring.boot.autoconfigure.OpenAPIValidationAdviceEnabled;
import com.ksoot.problem.spring.config.ProblemConfigException;
import com.ksoot.problem.spring.config.ProblemProperties;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ControllerAdvice} for handling OpenAPI validation exceptions in Servlet-based web
 * applications. It also provides beans for OpenAPI validation interceptor and filter.
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = {ProblemProperties.class})
@ConditionalOnClass(ValidationReportHandler.class)
@Conditional(OpenAPIValidationAdviceEnabled.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@RequiredArgsConstructor
public class OpenApiValidationExceptionHandler
    implements OpenApiValidationAdviceTrait<NativeWebRequest, ResponseEntity<ProblemDetail>> {

  private final ProblemProperties problemProperties;

  /**
   * Creates an {@link OpenApiValidationInterceptor} bean.
   *
   * @return the OpenAPI validation interceptor
   * @throws ProblemConfigException if the OpenAPI specification path is invalid
   */
  @Bean
  public OpenApiValidationInterceptor validationInterceptor() {
    if (StringUtils.isBlank(this.problemProperties.getOpenApi().getPath())) {
      throw new ProblemConfigException(
          "Invalid OpenAPI Spec file: " + this.problemProperties.getOpenApi().getPath());
    }
    OpenApiInteractionValidator validator =
        OpenApiInteractionValidator.createFor(this.problemProperties.getOpenApi().getPath())
            .build();
    return new OpenApiValidationInterceptor(validator);
  }

  /**
   * Creates an OpenAPI validation filter bean.
   *
   * @return the validation filter
   * @see PathConfigurableOpenApiValidationFilter
   */
  @Bean
  public Filter validationFilter() {
    return new PathConfigurableOpenApiValidationFilter(this.problemProperties.getOpenApi());
  }
}
