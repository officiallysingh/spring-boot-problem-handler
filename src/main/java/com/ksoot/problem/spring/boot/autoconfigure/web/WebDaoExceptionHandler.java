package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.spring.advice.dao.AbstractDaoExceptionHandler;
import com.ksoot.problem.spring.advice.dao.ConstraintNameResolver;
import com.ksoot.problem.spring.boot.autoconfigure.DaoAdviceEnabled;
import com.ksoot.problem.spring.boot.autoconfigure.ORMAdviceEnabled;
import com.ksoot.problem.spring.config.ProblemProperties;
import java.util.List;
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
 * {@link ControllerAdvice} for handling DAO-related exceptions in Servlet-based web applications.
 *
 * @author Rajveer Singh
 * @see AbstractDaoExceptionHandler
 */
@Configuration
@EnableConfigurationProperties(ProblemProperties.class)
@Conditional(value = {DaoAdviceEnabled.class, ORMAdviceEnabled.class})
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class WebDaoExceptionHandler
    extends AbstractDaoExceptionHandler<NativeWebRequest, ResponseEntity<ProblemDetail>> {

  /**
   * Constructs a new web DAO exception handler with the given resolvers and environment.
   *
   * @param constraintNameResolvers the list of constraint name resolvers
   * @param env the application environment
   */
  WebDaoExceptionHandler(
      final List<ConstraintNameResolver> constraintNameResolvers, final Environment env) {
    super(constraintNameResolvers, env);
  }
}
