package com.ksoot.problem.spring.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for problem handling.
 *
 * @author Rajveer Singh
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "problem")
public class ProblemProperties {

  /** Whether to enable Problem handling. Default: {@code true}. */
  private boolean enabled = true;

  /** Help page base URL. Default: {@code http://localhost:8080/problems/help.html}. */
  private String typeUrl = "http://localhost:8080/problems/help.html";

  /**
   * Whether to include debug-info such as message codes etc. in error response messages. Default:
   * {@code false}.
   */
  private boolean debugEnabled = false;

  /** Whether to include stacktrace in error response messages. Default: {@code false}. */
  private boolean stacktraceEnabled = false;

  /** Whether to include exception cause in error response messages. Default: {@code false}. */
  private boolean causeChainsEnabled = false;

  /** Whether to enable Jackson Problem module. Default: {@code true}. */
  private boolean jacksonModuleEnabled = true;

  /** Whether to enable DAO exception handling advices. Default: {@code true}. */
  private boolean daoAdviceEnabled = true;

  /** Whether to enable Security exception handling advices. Default: {@code true}. */
  private boolean securityAdviceEnabled = true;

  /** OpenAPI validation properties. */
  private OpenApi openApi = new OpenApi();

  /** Tracing properties. */
  private Tracing tracing = new Tracing();

  /** Configuration properties for OpenAPI validation. */
  @Getter
  @Setter
  @NoArgsConstructor
  @ToString
  @Valid
  public static class OpenApi {

    /** Path of API Specification JSON file. Default: {@code /oas/api.json}. */
    private String path = "/oas/api.json";

    /**
     * List of path patterns in ant-pattern format to exclude from OpenAPI Specification validation.
     */
    private List<String> excludePatterns = new ArrayList<>();

    /**
     * Whether to enable OpenAPI request validation. While enabling, ensure Problem is also enabled.
     * Default: {@code false}.
     */
    private boolean reqValidationEnabled = false;

    /**
     * Whether to enable OpenAPI response validation. While enabling, ensure Problem is also
     * enabled. Default: {@code false}.
     */
    private boolean resValidationEnabled = false;
  }

  /** Configuration properties for tracing support in error responses. */
  @Getter
  @Setter
  @NoArgsConstructor
  @ToString
  @Valid
  public static class Tracing {

    /** Whether to enable tracing support. Default: {@code false}. */
    private boolean enabled;

    /**
     * Attribute name in error response body or header name for Trace Id. Default: {@code
     * X-trace-id}.
     */
    @NotEmpty private String traceId = "X-trace-id";

    /**
     * Whether to add Trace Id in header or body of error response. Default: {@link
     * Strategy#HEADER}.
     */
    @NotNull private Strategy strategy = Strategy.HEADER;

    /** Strategy for including Trace Id in the response. */
    public enum Strategy {
      /** Include Trace Id in the response header. */
      HEADER,
      /** Include Trace Id in the response body. */
      BODY;

      /**
       * Checks if the strategy is {@link #HEADER}.
       *
       * @return {@code true} if header strategy
       */
      public boolean isHeader() {
        return this == HEADER;
      }

      /**
       * Checks if the strategy is {@link #BODY}.
       *
       * @return {@code true} if body strategy
       */
      public boolean isBody() {
        return this == BODY;
      }
    }
  }
}
