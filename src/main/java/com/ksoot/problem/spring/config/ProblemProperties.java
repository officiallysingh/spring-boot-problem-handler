package com.ksoot.problem.spring.config;

import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

  private boolean jacksonModuleEnabled = true;

  private boolean daoAdviceEnabled = true;

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
