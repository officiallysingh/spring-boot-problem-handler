package com.pchf.problem.spring.boot.autoconfigure.web;

import com.pchf.problem.ProblemDetails;
import com.pchf.problem.core.ErrorResponseBuilder;
import com.pchf.problem.core.MediaTypes;
import com.pchf.problem.core.Problem;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

class SpringWebErrorResponseBuilder implements ErrorResponseBuilder<NativeWebRequest, ResponseEntity<ProblemDetails>> {

  @SneakyThrows(HttpMediaTypeNotAcceptableException.class)
  public static Optional<MediaType> negotiate(final NativeWebRequest request) {
    final ContentNegotiationStrategy negotiator = DEFAULT_CONTENT_NEGOTIATION_STRATEGY;
    final List<MediaType> mediaTypes = negotiator.resolveMediaTypes(request);
    return ErrorResponseBuilder.getProblemMediaType(mediaTypes);
  }

  @Override
  public ResponseEntity<ProblemDetails> buildResponse(final Throwable throwable, final NativeWebRequest request,
                                                      final HttpStatus status, final HttpHeaders headers, final Problem... problems) {
    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
      request.setAttribute(ERROR_EXCEPTION, throwable, SCOPE_REQUEST);
    }

    ProblemDetails problemDetails = ProblemDetails.of(this.requestUri(request), this.requestMethod(request),
        problems);
    Optional<ResponseEntity<ProblemDetails>> responseEntity = negotiate(request).map(contentType -> ResponseEntity
        .status(status).headers(headers).contentType(contentType).body(problemDetails));
    if (responseEntity.isPresent()) {
      return postProcess(responseEntity.get(), request);
    } else {
      return fallback(request, status, headers, problems);
    }
  }

  private ResponseEntity<ProblemDetails> fallback(final NativeWebRequest request, final HttpStatus status,
                                                  final HttpHeaders headers, final Problem... problems) {
    ProblemDetails problemDetails = ProblemDetails.of(this.requestUri(request), this.requestMethod(request),
        problems);
    return ResponseEntity.status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(problemDetails);
  }

  private ResponseEntity<ProblemDetails> postProcess(final ResponseEntity<ProblemDetails> errorResponse,
                                                     final NativeWebRequest request) {
    return errorResponse;
  }

  private URI requestUri(final NativeWebRequest request) {
    return URI.create(request.getNativeRequest(HttpServletRequest.class).getRequestURI());
  }

  private HttpMethod requestMethod(final NativeWebRequest request) {
    return HttpMethod.valueOf(request.getNativeRequest(HttpServletRequest.class).getMethod());
  }
}
