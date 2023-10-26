package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.ksoot.problem.spring.config.ProblemProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.AntPathMatcher;

public class PathConfigurableOpenApiValidationFilter extends OpenApiValidationFilter {

  private final AntPathMatcher pathMatcher;

  private final String openApiLocation;

  private final List<String> excludePatterns;

  public PathConfigurableOpenApiValidationFilter(
      final ProblemProperties.OpenApi openApiProperties) {
    super(openApiProperties.isReqValidationEnabled(), openApiProperties.isResValidationEnabled());
    this.openApiLocation = openApiProperties.getPath();
    this.pathMatcher = new AntPathMatcher();
    this.excludePatterns = openApiProperties.getExcludePatterns();
  }

  @Override
  protected boolean shouldNotFilter(final HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    boolean excludedPath =
        CollectionUtils.isNotEmpty(this.excludePatterns)
            ? this.excludePatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath))
            : false;
    return this.pathMatcher.match("/**/v3/api-docs", requestPath)
        || this.pathMatcher.match("/v3/api-docs", requestPath)
        || this.pathMatcher.match("/v3/api-docs/*", requestPath)
        || this.pathMatcher.match("/swagger-ui.html", requestPath)
        || this.pathMatcher.match("/**/swagger-ui.html", requestPath)
        || this.pathMatcher.match("/swagger-ui/*", requestPath)
        || this.pathMatcher.match("/**/swagger-ui/*", requestPath)
        || this.pathMatcher.match("/**" + this.openApiLocation, requestPath)
        || this.pathMatcher.match(this.openApiLocation, requestPath)
        || excludedPath;
  }
}
