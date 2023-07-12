package com.pchf.problem.spring.boot.autoconfigure;

import jakarta.validation.Valid;
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

  private boolean debugEnabled = false;

  private boolean causeChainsEnabled = false;

  private boolean stacktraceEnabled = false;

  private boolean ormAdviceEnabled = true;

  private boolean securityAdviceEnabled = true;

  private boolean jacksonModuleEnabled = true;

  private OpenApi openApi = new OpenApi();

  @Getter
  @Setter
  @NoArgsConstructor
  @ToString
  @Valid
  public static class OpenApi {

    private String path = "/oas/api.json";

    private boolean reqValidationEnabled = false;

    private boolean resValidationEnabled = false;
  }
}
