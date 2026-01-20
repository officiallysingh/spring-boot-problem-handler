package com.ksoot.problem.spring.advice.io;

import com.google.common.base.CharMatcher;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Advice trait to handle {@link DataBufferLimitException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see DataBufferLimitException
 * @see HttpStatus#BAD_REQUEST
 */
public interface DataBufferLimitExceptionAdviceTrait<T, R> extends AdviceTrait<T, R> {

  //  org.springframework.core.io.buffer.DataBufferLimitException: Part exceeded the disk usage
  // limit of 1024 bytes

  /**
   * Handles {@link DataBufferLimitException} and converts it into a response.
   *
   * @param exception the data buffer limit exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleDataBufferLimitException(
      final DataBufferLimitException exception, final T request) {
    String errorKey = ClassUtils.getName(exception);
    String defaultMessage = exception.getMessage();
    long bytes = -1;
    try {
      final String byteSizeString = CharMatcher.inRange('0', '9').retainFrom(defaultMessage);
      bytes = StringUtils.isNotBlank(byteSizeString) ? Long.parseLong(byteSizeString) : bytes;
    } catch (final Exception ex) {
      // Ignore on purpose
    }

    String maxFileSizeAllowed = bytes != -1 ? DataSize.ofBytes(bytes).toString() : "UNKNOWN";

    String detailCode = ProblemConstant.DETAIL_CODE_PREFIX + errorKey;

    Problem problem =
        toProblem(
            exception,
            HttpStatus.BAD_REQUEST,
            ProblemMessageSourceResolver.of(
                detailCode, defaultMessage, new Object[] {maxFileSizeAllowed}));

    return toResponse(exception, request, HttpStatus.BAD_REQUEST, problem);
  }
}
