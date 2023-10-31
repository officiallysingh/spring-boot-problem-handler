package com.ksoot.problem.spring.boot.autoconfigure.webflux;

import com.ksoot.problem.spring.advice.dao.AbstractDaoExceptionHandler;
import com.ksoot.problem.spring.advice.dao.ConstraintNameResolver;
import com.ksoot.problem.spring.boot.autoconfigure.DaoAdviceEnabled;
import com.ksoot.problem.spring.config.ProblemProperties;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * @author Rajveer Singh
 */
@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(
    prefix = "problem",
    name = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Conditional(DaoAdviceEnabled.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class WebFluxDaoExceptionHandler
    extends AbstractDaoExceptionHandler<NativeWebRequest, ResponseEntity<ProblemDetail>> {

  WebFluxDaoExceptionHandler(
      final List<ConstraintNameResolver> constraintNameResolvers, final Environment env) {
    super(constraintNameResolvers, env);
  }
}
