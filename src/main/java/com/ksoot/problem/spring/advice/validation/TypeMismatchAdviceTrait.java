package com.ksoot.problem.spring.advice.validation;

import static com.ksoot.problem.core.ProblemConstant.PROPERTY_PATH_KEY;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link TypeMismatchException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see TypeMismatchException
 * @see BaseValidationAdviceTrait#defaultConstraintViolationStatus()
 */
public interface TypeMismatchAdviceTrait<T, R> extends BaseValidationAdviceTrait<T, R> {

  /**
   * Handles {@link TypeMismatchException} and converts it into a response.
   *
   * @param exception the type mismatch exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleTypeMismatch(final TypeMismatchException exception, final T request) {

    HttpStatus status = defaultConstraintViolationStatus();
    String propertyName = exception.getPropertyName();

    String errorKey = exception.getErrorCode() + ProblemConstant.DOT + propertyName;

    String codeCode =
        ProblemConstant.CODE_CODE_PREFIX
            + GeneralErrorKey.TYPE_MISMATCH
            + ProblemConstant.DOT
            + errorKey;
    String titleCode =
        ProblemConstant.TITLE_CODE_PREFIX
            + GeneralErrorKey.TYPE_MISMATCH
            + ProblemConstant.DOT
            + errorKey;
    String detailCode =
        ProblemConstant.DETAIL_CODE_PREFIX
            + GeneralErrorKey.TYPE_MISMATCH
            + ProblemConstant.DOT
            + errorKey;

    Map<String, Object> parameters = new LinkedHashMap<>(4);
    parameters.put(PROPERTY_PATH_KEY, propertyName);

    Problem problem =
        toProblem(
            exception,
            ProblemMessageSourceResolver.of(codeCode, status.value()),
            ProblemMessageSourceResolver.of(titleCode, status.getReasonPhrase()),
            ProblemMessageSourceResolver.of(
                detailCode, exception.getMostSpecificCause().toString()),
            parameters);

    return toResponse(exception, request, status, problem);
  }
}
