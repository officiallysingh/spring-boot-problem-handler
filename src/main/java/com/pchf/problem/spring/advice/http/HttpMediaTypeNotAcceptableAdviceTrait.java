package com.pchf.problem.spring.advice.http;

import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemConstant;
import com.pchf.problem.spring.advice.AdviceTrait;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
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
        ProblemMessageSourceResolver.of(ProblemConstant.MESSAGE_CODE_PREFIX + GeneralErrorKey.MEDIA_TYPE_NOT_ACCEPTABLE,
            "Media Type Not Acceptable, except: {0}", new Object[]{MimeTypeUtils.toString(supportedMediaTypes)}),
        ProblemMessageSourceResolver.of(ProblemConstant.DETAILS_CODE_PREFIX + GeneralErrorKey.MEDIA_TYPE_NOT_ACCEPTABLE,
            exception.getMessage()));
    return create(exception, request, HttpStatus.NOT_ACCEPTABLE, problem);
  }
}
