package com.ksoot.problem.spring.advice.application;

import static com.ksoot.problem.core.ProblemConstant.CODE_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.DETAIL_CODE_PREFIX;
import static com.ksoot.problem.core.ProblemConstant.ERRORS_KEY;
import static com.ksoot.problem.core.ProblemConstant.TITLE_CODE_PREFIX;

import com.google.common.collect.Lists;
import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.MultiProblem;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.core.ProblemSupport;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see AdviceTrait
 */
public interface ApplicationMultiProblemAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMultiProblem(final MultiProblem exception, final T request) {
    List<Problem> problems = Lists.newArrayList();

    if (CollectionUtils.isNotEmpty(exception.getErrors())) {
      problems.addAll(
          exception.getErrors().stream()
              .map(
                  ex -> {
                    switch (ex) {
                      case Problem problem -> {
                        return problem;
                      }
                      case ProblemSupport problemSupport -> {
                        if (Objects.nonNull(problemSupport.getProblem())) {
                          return problemSupport.getProblem();
                        } else {
                          String errorKey = problemSupport.getErrorKey();
                          String detailCode = ProblemConstant.DETAIL_CODE_PREFIX + errorKey;

                          return toProblem(
                              (Throwable) ex,
                              problemSupport.getStatus(),
                              errorKey,
                              Optional.ofNullable(problemSupport.getDefaultDetail())
                                  .orElse(detailCode),
                              problemSupport.getDetailArgs(),
                              Optional.ofNullable(problemSupport.getParameters())
                                  .orElse(Collections.emptyMap()));
                        }
                      }
                      case Throwable throwable -> {
                        return toProblem(
                            throwable,
                            GeneralErrorKey.INTERNAL_SERVER_ERROR,
                            HttpStatus.INTERNAL_SERVER_ERROR);
                      }
                      case null, default ->
                          throw new IllegalStateException(
                              "MultiProblem contain illegal instance: " + ex);
                    }
                  })
              .toList());
    }
    Map<String, Object> parameters = new LinkedHashMap<>(problems.size() + 5);
    parameters.put(ERRORS_KEY, problems);

    HttpStatus status = exception.getStatus();
    ProblemMessageSourceResolver codeResolver =
        ProblemMessageSourceResolver.of(
            CODE_CODE_PREFIX + GeneralErrorKey.MULTIPLE_ERRORS, status.value());
    ProblemMessageSourceResolver titleResolver =
        ProblemMessageSourceResolver.of(
            TITLE_CODE_PREFIX + GeneralErrorKey.MULTIPLE_ERRORS, status.getReasonPhrase());
    ProblemMessageSourceResolver detailResolver =
        ProblemMessageSourceResolver.of(
            DETAIL_CODE_PREFIX + GeneralErrorKey.MULTIPLE_ERRORS, exception.getMessage());

    Problem problem = toProblem(exception, codeResolver, titleResolver, detailResolver, parameters);
    return toResponse(exception, request, exception.getStatus(), problem);
  }
}
