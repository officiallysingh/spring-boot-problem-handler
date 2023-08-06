package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.MediaTypes;
import com.ksoot.problem.core.Problem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

class SpringWebErrorResponseBuilder implements ErrorResponseBuilder<NativeWebRequest, ResponseEntity<ProblemDetail>> {

  @SneakyThrows(HttpMediaTypeNotAcceptableException.class)
  public static Optional<MediaType> negotiate(final NativeWebRequest request) {
    final ContentNegotiationStrategy negotiator = DEFAULT_CONTENT_NEGOTIATION_STRATEGY;
    final List<MediaType> mediaTypes = negotiator.resolveMediaTypes(request);
    return ErrorResponseBuilder.getProblemMediaType(mediaTypes);
  }

  @Override
  public ResponseEntity<ProblemDetail> buildResponse(final Throwable throwable, final NativeWebRequest request,
                                                     final HttpStatus status, final HttpHeaders headers, final Problem problem) {
    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
      request.setAttribute(ERROR_EXCEPTION, throwable, SCOPE_REQUEST);
    }

    ProblemDetail problemDetail = createProblemDetail(request, status, problem);
    Optional<ResponseEntity<ProblemDetail>> responseEntity = negotiate(request).map(contentType -> ResponseEntity
        .status(status).headers(headers).contentType(contentType).body(problemDetail));

    if (responseEntity.isPresent()) {
      return postProcess(responseEntity.get(), request);
    } else {
      return fallback(request, status, headers, problem);
    }
  }

  private ResponseEntity<ProblemDetail> fallback(final NativeWebRequest request, final HttpStatus status,
                                                 final HttpHeaders headers, final Problem problem) {
    ProblemDetail problemDetail = createProblemDetail(request, status, problem);
    return ResponseEntity.status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(problemDetail);
  }

  private ResponseEntity<ProblemDetail> postProcess(final ResponseEntity<ProblemDetail> errorResponse,
                                                    final NativeWebRequest request) {
    return errorResponse;
  }

  @Override
  public URI requestUri(final NativeWebRequest request) {
    return URI.create(request.getNativeRequest(HttpServletRequest.class).getRequestURI());
  }

  @Override
  public HttpMethod requestMethod(final NativeWebRequest request) {
    return HttpMethod.valueOf(request.getNativeRequest(HttpServletRequest.class).getMethod());
  }
}
