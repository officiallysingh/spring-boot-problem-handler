package com.ksoot.problem.spring.advice.validation;

import static com.ksoot.problem.core.ProblemConstant.CODE_RESOLVER;
import static com.ksoot.problem.core.ProblemConstant.DETAIL_RESOLVER;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ViolationVM {

  private String code;

  private String detail;

  private String propertyPath;

  private Map<String, Object> parameters;

  public static ViolationVM of(final String detail, final String propertyPath) {
    ViolationVM violationVM = new ViolationVM();
    violationVM.detail = detail;
    violationVM.propertyPath = propertyPath;
    return violationVM;
  }

  public static ViolationVM of(final String code, final String detail, final String propertyPath) {
    ViolationVM violationVM = new ViolationVM();
    violationVM.code = code;
    violationVM.detail = detail;
    violationVM.propertyPath = propertyPath;
    return violationVM;
  }

  public static ViolationVM of(
      final String code,
      final String detail,
      final String propertyPath,
      MessageSourceResolvable codeResolver,
      MessageSourceResolvable messageResolver) {
    ViolationVM violationVM = of(code, detail, propertyPath);

    Map<String, Object> parameters = new LinkedHashMap<>(2);
    parameters.put(CODE_RESOLVER, codeResolver);
    parameters.put(DETAIL_RESOLVER, messageResolver);
    violationVM.parameters = parameters;

    return violationVM;
  }

  public static ViolationVM of(
      final String code,
      final String message,
      final String propertyPath,
      MessageSourceResolvable messageResolver) {
    ViolationVM violationVM = of(code, message, propertyPath);

    Map<String, Object> parameters = new LinkedHashMap<>(1);
    parameters.put(DETAIL_RESOLVER, messageResolver);
    violationVM.parameters = parameters;

    return violationVM;
  }

  @JsonAnyGetter
  public Map<String, Object> getParameters() {
    return this.parameters;
  }
}
