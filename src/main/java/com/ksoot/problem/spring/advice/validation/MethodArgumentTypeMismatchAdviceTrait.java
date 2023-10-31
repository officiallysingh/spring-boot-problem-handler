package com.ksoot.problem.spring.advice.validation;

import static com.ksoot.problem.core.ProblemConstant.PROPERTY_PATH_KEY;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

public interface MethodArgumentTypeMismatchAdviceTrait<T, R>
    extends BaseValidationAdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMethodArgumentTypeMismatch(
      final MethodArgumentTypeMismatchException exception, final T request) {

    HttpStatus status = defaultConstraintViolationStatus();
    String parameterName = exception.getParameter().getParameterName();

    String errorKey =
        exception.getParameter().getContainingClass().getSimpleName()
            + ProblemConstant.DOT
            + exception.getParameter().getMethod().getName()
            + ProblemConstant.DOT
            + parameterName;

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
    parameters.put(PROPERTY_PATH_KEY, parameterName);

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
