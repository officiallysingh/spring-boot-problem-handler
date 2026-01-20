package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.atlassian.oai.validator.springmvc.OpenApiValidationFilter;
import com.ksoot.problem.spring.config.ProblemProperties;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.AntPathMatcher;

/**
 * An {@link OpenApiValidationFilter} that allows configuring paths to exclude from OpenAPI
 * validation.
 */
public class PathConfigurableOpenApiValidationFilter extends OpenApiValidationFilter {

  private final AntPathMatcher pathMatcher;

  private final String openApiLocation;

  private final List<String> excludePatterns;

  /**
   * Constructs a new path configurable OpenAPI validation filter with the given properties.
   *
   * @param openApiProperties the OpenAPI properties
   */
  public PathConfigurableOpenApiValidationFilter(
      final ProblemProperties.OpenApi openApiProperties) {
    super(openApiProperties.isReqValidationEnabled(), openApiProperties.isResValidationEnabled());
    this.openApiLocation = openApiProperties.getPath();
    this.pathMatcher = new AntPathMatcher();
    this.excludePatterns = openApiProperties.getExcludePatterns();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean shouldNotFilter(final HttpServletRequest request) {
    String requestPath = request.getRequestURI();
    boolean excludedPath =
        CollectionUtils.isNotEmpty(this.excludePatterns)
            && this.excludePatterns.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, requestPath));

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
