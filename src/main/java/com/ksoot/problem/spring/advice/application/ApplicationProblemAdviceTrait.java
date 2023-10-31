package com.ksoot.problem.spring.advice.application;

import com.ksoot.problem.core.ApplicationProblem;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import java.util.Collections;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see AdviceTrait
 */
public interface ApplicationProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleApplicationProblem(final ApplicationProblem exception, final T request) {
    HttpStatus status = exception.getStatus();
    if (StringUtils.isNotBlank(exception.getErrorKey())) {
      String errorKey = exception.getErrorKey();
      String detailCode = ProblemConstant.DETAIL_CODE_PREFIX + errorKey;

      Problem problem =
          toProblem(
              exception,
              exception.getStatus(),
              errorKey,
              Optional.ofNullable(exception.getDefaultDetail()).orElse(detailCode),
              exception.getDetailArgs(),
              Optional.ofNullable(exception.getParameters()).orElse(Collections.emptyMap()));

      return toResponse(exception, request, status, problem);
    } else {
      return toResponse(exception, request, exception.getStatus(), exception.getProblem());
    }
  }
}
