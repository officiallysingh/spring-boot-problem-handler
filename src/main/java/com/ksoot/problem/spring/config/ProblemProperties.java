package com.ksoot.problem.spring.config;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rajveer Singh
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@ConfigurationProperties(prefix = "problem")
public class ProblemProperties {

  private boolean enabled = true;

  private String typeUrl;

  private boolean debugEnabled = false;

  private boolean stacktraceEnabled = false;

  private boolean causeChainsEnabled = false;

  private boolean ormAdviceEnabled = true;

  private boolean securityAdviceEnabled = true;

  private OpenApi openApi = new OpenApi();

  @Getter
  @Setter
  @NoArgsConstructor
  @ToString
  @Valid
  public static class OpenApi {

    private String path = "/oas/api.json";

    private List<String> excludePatterns = new ArrayList<>();

    private boolean reqValidationEnabled = false;

    private boolean resValidationEnabled = false;
  }
}
