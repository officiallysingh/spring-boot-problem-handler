package com.ksoot.problem.spring.boot.autoconfigure.web;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.MediaTypes;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.*;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.context.request.NativeWebRequest;

@RequiredArgsConstructor
public class SpringWebErrorResponseBuilder
    implements ErrorResponseBuilder<NativeWebRequest, ResponseEntity<ProblemDetail>> {

  private final TraceProvider traceProvider;

  @SneakyThrows(HttpMediaTypeNotAcceptableException.class)
  public static Optional<MediaType> negotiate(final NativeWebRequest request) {
    final List<MediaType> mediaTypes =
        DEFAULT_CONTENT_NEGOTIATION_STRATEGY.resolveMediaTypes(request);
    return ErrorResponseBuilder.getProblemMediaType(mediaTypes);
  }

  @Override
  public ResponseEntity<ProblemDetail> buildResponse(
      final Throwable throwable,
      final NativeWebRequest request,
      final HttpStatus status,
      final HttpHeaders headers,
      final Problem problem) {
    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
      request.setAttribute(ERROR_EXCEPTION, throwable, SCOPE_REQUEST);
    }

    ProblemDetail problemDetail = createProblemDetail(request, status, problem, this.traceProvider);
    Optional<ResponseEntity<ProblemDetail>> responseEntity =
        negotiate(request)
            .map(
                contentType ->
                    ResponseEntity.status(status)
                        .headers(headers)
                        .contentType(contentType)
                        .body(problemDetail));

    return responseEntity
        .map(problemDetailResponseEntity -> postProcess(problemDetailResponseEntity, request))
        .orElseGet(() -> fallback(request, status, headers, problem));
  }

  private ResponseEntity<ProblemDetail> postProcess(
      final ResponseEntity<ProblemDetail> errorResponse, final NativeWebRequest request) {
    return errorResponse;
  }

  private ResponseEntity<ProblemDetail> fallback(
      final NativeWebRequest request,
      final HttpStatus status,
      final HttpHeaders headers,
      final Problem problem) {
    ProblemDetail problemDetail = createProblemDetail(request, status, problem, this.traceProvider);
    return ResponseEntity.status(status)
        .headers(headers)
        .contentType(MediaTypes.PROBLEM)
        .body(problemDetail);
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
