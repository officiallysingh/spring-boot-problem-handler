package com.pchf.problem.spring.advice.validation;

import com.atlassian.oai.validator.model.Request.Method;
import com.atlassian.oai.validator.report.ValidationReport;
import com.atlassian.oai.validator.report.ValidationReport.Level;
import com.atlassian.oai.validator.report.ValidationReport.Message;
import com.atlassian.oai.validator.report.ValidationReport.MessageContext;
import com.atlassian.oai.validator.springmvc.InvalidRequestException;
import com.atlassian.oai.validator.springmvc.InvalidResponseException;
import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.pchf.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_CODE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.VIOLATIONS_KEY;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public interface OpenApiValidationAdviceTrait<T, R> extends BaseValidationAdviceTrait<T, R> {

  @ExceptionHandler(InvalidRequestException.class)
  default R handleInvalidRequest(final InvalidRequestException exception, final T request) {
    final List<ViolationVM> violations = handleValidationReport(exception.getValidationReport(), exception);
    Map<String, Object> parameters = new LinkedHashMap<>(4);
    parameters.put(VIOLATIONS_KEY, violations);
    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_CODE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX), parameters);
    return create(exception, request, defaultConstraintViolationStatus(),
        problem);
  }

  @ExceptionHandler
  default R handleInvalidResponse(final InvalidResponseException exception, final T request) {
    final List<ViolationVM> violations = handleValidationReport(exception.getValidationReport(), exception);
    Map<String, Object> parameters = new LinkedHashMap<>(4);
    parameters.put(VIOLATIONS_KEY, violations);
    Problem problem = toProblem(exception, ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_CODE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX),
        ProblemMessageSourceResolver.of(CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX), parameters);
    return create(exception, request, defaultConstraintViolationStatus(),
        problem);
  }

  default List<ViolationVM> handleValidationReport(final ValidationReport validationReport, final Throwable exception) {
    return validationReport.getMessages().stream().filter(message -> message.getLevel() == Level.ERROR)
        .flatMap(message -> handleValidationReportMessage(message, exception).stream()).toList();
  }

  default List<ViolationVM> handleValidationReportMessage(final Message message, final Throwable exception) {

    HttpStatus status = defaultConstraintViolationStatus();
    String propertyPath = message.getContext().flatMap(MessageContext::getParameter).map(Parameter::getName)
        .orElse("");

    List<String[]> errorPropertyKeys = deriveOpenApiValidationErrorKeys(message);

    return errorPropertyKeys.stream().map(propertyKeys -> {
      String[] codeCodes = Arrays.stream(propertyKeys)
          .map(errorKey -> ProblemConstant.CODE_CODE_PREFIX + GeneralErrorKey.OPEN_API_VIOLATION + ProblemConstant.DOT + errorKey)
          .toArray(String[]::new);
      String[] messageCodes = Arrays.stream(propertyKeys)
          .map(errorKey -> ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.OPEN_API_VIOLATION + ProblemConstant.DOT + errorKey)
          .toArray(String[]::new);

      ProblemMessageSourceResolver codeResolver = ProblemMessageSourceResolver.of(codeCodes, "" + status.value());
      ProblemMessageSourceResolver messageResolver = ProblemMessageSourceResolver.of(messageCodes, message.getMessage());
      return createViolation(codeResolver, messageResolver, propertyPath);
    }).toList();
  }


  default List<String[]> deriveOpenApiValidationErrorKeys(final Message message) {
    String propertyKey = message.getContext().flatMap(MessageContext::getParameter).map(Parameter::getName)
        .orElse("");
    Method requestMetod = message.getContext().flatMap(MessageContext::getRequestMethod).orElse(null);
    String requestPath = message.getContext().flatMap(MessageContext::getRequestPath).orElse("").replaceAll("/",
        ProblemConstant.DOT);

    if (isEmpty(propertyKey)) {
      if (requestMetod == null || requestMetod == Method.GET) {
        List<String[]> errorKeys = new ArrayList<>(1);
        errorKeys.add(new String[]{message.getKey()});
        return errorKeys;
      } else {
        // Extract property key from message
        String messageText = message.getMessage();
        try {
          int i1 = messageText.lastIndexOf("([");
          int j1 = messageText.lastIndexOf("])");
          int i2 = messageText.indexOf("'/");
          int j2 = messageText.indexOf("']");
//					String objectPath = (i2 == -1 || j2 == -1) ? messageText.substring(i1 + 2, j1)
//							: String.join(DOT, Arrays.stream(messageText.substring(i2 + 2, j2).split("/"))
//									.filter(part -> !StringUtils.isNumeric(part)).toList());
          String objectPath = (i2 == -1 || j2 == -1) ? ""
              : String.join(ProblemConstant.DOT, Arrays.stream(messageText.substring(i2 + 2, j2).split("/"))
              .filter(part -> !StringUtils.isNumeric(part)).toList());
          if (i1 == -1) {
            List<String[]> errorKeys = new ArrayList<>(1);
            errorKeys.add(new String[]{
                message.getKey() + requestPath + ProblemConstant.DOT + requestMetod.name().toLowerCase() + ProblemConstant.DOT
                    + objectPath,
                message.getKey() + requestPath + ProblemConstant.DOT + objectPath, message.getKey() + ProblemConstant.DOT + objectPath,
                objectPath});
            return errorKeys;
          }
          List<String> properties = Arrays.asList(messageText.substring(i1 + 1, j1).replaceAll("\"", "")
              .replace("[", "").replace("]", "").split(",")).stream().toList();
          String objectPathPart = StringUtils.isNotBlank(objectPath) ? ProblemConstant.DOT + objectPath + ProblemConstant.DOT : ProblemConstant.DOT;
          return properties.stream()
              .map(property -> new String[]{
                  message.getKey() + requestPath + ProblemConstant.DOT + requestMetod.name().toLowerCase()
                      + objectPathPart + property,
                  message.getKey() + requestPath + objectPathPart + property,
                  message.getKey() + objectPathPart + property})
              .toList();
        } catch (Exception e) {
          List<String[]> errorKeys = new ArrayList<>(1);
          errorKeys.add(
              new String[]{message.getKey() + requestPath + ProblemConstant.DOT + requestMetod.name().toLowerCase(),
                  message.getKey() + requestPath, message.getKey()});
          return errorKeys;
        }
      }
    } else {
      List<String[]> errorKeys = new ArrayList<>(1);
      errorKeys.add(new String[]{
          message.getKey() + requestPath + ProblemConstant.DOT + requestMetod.name().toLowerCase() + ProblemConstant.DOT + propertyKey,
          message.getKey() + requestPath + ProblemConstant.DOT + propertyKey, message.getKey() + ProblemConstant.DOT + propertyKey});
      return errorKeys;
    }
  }
}
