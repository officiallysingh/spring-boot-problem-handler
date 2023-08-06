package com.ksoot.problem.spring.advice.http;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * @see HttpMediaTypeNotAcceptableException
 * @see HttpStatus#NOT_ACCEPTABLE
 */
public interface HttpMediaTypeNotAcceptableAdviceTrait<T, R> extends AdviceTrait<T, R> {

  @ExceptionHandler
  default R handleMediaTypeNotAcceptable(final HttpMediaTypeNotAcceptableException exception, final T request) {
    List<MediaType> supportedMediaTypes = exception.getSupportedMediaTypes();
    Problem problem = toProblem(exception, HttpStatus.NOT_ACCEPTABLE,
        ProblemMessageSourceResolver.of(ProblemConstant.DETAIL_CODE_PREFIX + GeneralErrorKey.MEDIA_TYPE_NOT_ACCEPTABLE,
            "Media Type Not Acceptable, except: {0}", new Object[]{MimeTypeUtils.toString(supportedMediaTypes)}));
    return create(exception, request, HttpStatus.NOT_ACCEPTABLE, problem);
  }
}
