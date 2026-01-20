package com.ksoot.problem.spring.advice.http;

import static com.ksoot.problem.core.ProblemConstant.DETAIL_CODE_PREFIX;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;

/**
 * Base advice trait for handling "Not Acceptable" and "Unsupported Media Type" errors.
 *
 * @param <T> the request type
 * @param <R> the response type
 */
public interface BaseNotAcceptableAdviceTrait<T, R> extends AdviceTrait<T, R> {

  /**
   * Processes a media type not supported exception and builds a response.
   *
   * @param supportedMediaTypes the list of supported media types
   * @param causeMediaType the media type that caused the exception
   * @param exception the exception
   * @param request the request
   * @return the error response
   */
  default R processMediaTypeNotSupportedException(
      final List<MediaType> supportedMediaTypes,
      final MediaType causeMediaType,
      final Exception exception,
      final T request) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setAccept(supportedMediaTypes);
    Problem problem =
        toProblem(
            exception,
            HttpStatus.UNSUPPORTED_MEDIA_TYPE,
            ProblemMessageSourceResolver.of(
                DETAIL_CODE_PREFIX + GeneralErrorKey.MEDIA_TYPE_NOT_SUPPORTED,
                "Media Type: {0} Not Acceptable, Supported Media Types are: {1}",
                new Object[] {causeMediaType, MimeTypeUtils.toString(supportedMediaTypes)}));
    return buildResponse(exception, request, HttpStatus.UNSUPPORTED_MEDIA_TYPE, headers, problem);
  }
}
