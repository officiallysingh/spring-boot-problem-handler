package com.pchf.problem.spring.advice.http;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;

import java.util.List;

import static com.pchf.problem.core.ProblemConstant.DETAIL_CODE_PREFIX;

public interface BaseNotAcceptableAdviceTrait<T, R> extends AdviceTrait<T, R> {

  default R processMediaTypeNotSupportedException(final List<MediaType> supportedMediaTypes, final MediaType causeMediaType,
                                                  final Exception exception, final T request) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setAccept(supportedMediaTypes);
    Problem problem = toProblem(exception, HttpStatus.UNSUPPORTED_MEDIA_TYPE,
        ProblemMessageSourceResolver.of(DETAIL_CODE_PREFIX + GeneralErrorKey.MEDIA_TYPE_NOT_SUPPORTED,
            "Media Type: {0} Not Acceptable, Supported Media Types are: {1}", new Object[]{causeMediaType,
                MimeTypeUtils.toString(supportedMediaTypes)}));
    return create(exception, request, HttpStatus.UNSUPPORTED_MEDIA_TYPE, headers, problem);
  }
}
