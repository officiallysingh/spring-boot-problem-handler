package com.ksoot.problem.spring.advice.io;

import com.google.common.base.CharMatcher;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

/**
 * Advice trait to handle {@link MaxUploadSizeExceededException}s.
 *
 * @param <T> the request type
 * @param <R> the response type
 * @see MaxUploadSizeExceededException
 * @see HttpStatus#BAD_REQUEST
 */
public interface MaxUploadSizeExceededExceptionAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Handles {@link MaxUploadSizeExceededException} and converts it into a response.
   *
   * @param exception the max upload size exceeded exception
   * @param request the request
   * @return the error response
   */
  @ExceptionHandler
  default R handleMaxUploadSizeExceededException(
      final MaxUploadSizeExceededException exception, final T request) {
    String errorKey = ClassUtils.getName(exception);
    String defaultMessage = exception.getMessage();
    long bytes = exception.getMaxUploadSize();
    if (bytes == -1 && exception.getCause() instanceof IllegalStateException e) {
      try {
        defaultMessage = e.getMessage();
        final String byteSizeString =
            defaultMessage.substring(
                defaultMessage.lastIndexOf("(") + 1, defaultMessage.lastIndexOf(")"));
        bytes = StringUtils.isNotBlank(byteSizeString) ? Long.parseLong(byteSizeString) : bytes;
      } catch (final Exception ex) {
        // Ignore on purpose
      }
    }
    if (bytes == -1
        && exception.getMostSpecificCause() instanceof FileSizeLimitExceededException e) {
      try {
        defaultMessage = e.getMessage();
        final String byteSizeString = CharMatcher.inRange('0', '9').retainFrom(defaultMessage);
        bytes = StringUtils.isNotBlank(byteSizeString) ? Long.parseLong(byteSizeString) : bytes;
      } catch (final Exception ex) {
        // Ignore on purpose
      }
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
