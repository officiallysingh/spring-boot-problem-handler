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

/** View model for a validation violation. */
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ViolationVM {

  /** The violation code. */
  private String code;

  /** The violation detail message. */
  private String detail;

  /** The property path where the violation occurred. */
  private String propertyPath;

  /** Additional parameters for the violation. */
  private Map<String, Object> parameters;

  /**
   * Creates a new violation with the given detail and property path.
   *
   * @param detail the detail
   * @param propertyPath the property path
   * @return the violation view model
   */
  public static ViolationVM of(final String detail, final String propertyPath) {
    ViolationVM violationVM = new ViolationVM();
    violationVM.detail = detail;
    violationVM.propertyPath = propertyPath;
    return violationVM;
  }

  /**
   * Creates a new violation with the given code, detail, and property path.
   *
   * @param code the code
   * @param detail the detail
   * @param propertyPath the property path
   * @return the violation view model
   */
  public static ViolationVM of(final String code, final String detail, final String propertyPath) {
    ViolationVM violationVM = new ViolationVM();
    violationVM.code = code;
    violationVM.detail = detail;
    violationVM.propertyPath = propertyPath;
    return violationVM;
  }

  /**
   * Creates a new violation with the given details and resolvers.
   *
   * @param code the code
   * @param detail the detail
   * @param propertyPath the property path
   * @param codeResolver the code resolver
   * @param messageResolver the message resolver
   * @return the violation view model
   */
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

  /**
   * Creates a new violation with the given details and message resolver.
   *
   * @param code the code
   * @param message the message
   * @param propertyPath the property path
   * @param messageResolver the message resolver
   * @return the violation view model
   */
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

  /**
   * Returns the additional parameters.
   *
   * @return the parameters
   */
  @JsonAnyGetter
  public Map<String, Object> getParameters() {
    return this.parameters;
  }
}
