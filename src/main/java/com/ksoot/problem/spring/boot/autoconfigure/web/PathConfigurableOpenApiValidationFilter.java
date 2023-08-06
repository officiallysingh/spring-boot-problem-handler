package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.ksoot.problem.spring.boot.autoconfigure.ProblemProperties;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.AntPathMatcher;

public class PathConfigurableOpenApiValidationFilter extends OpenApiValidationFilter {

  private AntPathMatcher matcher = new AntPathMatcher();

  private String openApiLocation;

  public PathConfigurableOpenApiValidationFilter(final ProblemProperties.OpenApi openProperties) {
    super(openProperties.isReqValidationEnabled(), openProperties.isResValidationEnabled());
    this.openApiLocation = openProperties.getPath();
  }

  //	@Override
  protected boolean shouldNotFilter(final HttpServletRequest request) {
    String path = request.getRequestURI();
    return this.matcher.match("/**/v3/api-docs", path) || this.matcher.match("/v3/api-docs", path)
        || this.matcher.match("/v3/api-docs/*", path)
        || this.matcher.match("/swagger-ui.html", path)
        || this.matcher.match("/**/swagger-ui.html", path)
        || this.matcher.match("/swagger-ui/*", path)
        || this.matcher.match("/**/swagger-ui/*", path)
        || this.matcher.match("/**" + this.openApiLocation, path)
        || this.matcher.match(this.openApiLocation, path);
  }
}
