package com.ksoot.problem.spring.boot.autoconfigure.webflux;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.MediaTypes;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.accept.HeaderContentTypeResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * {@link ErrorResponseBuilder} implementation for WebFlux applications.
 *
 * @see ErrorResponseBuilder
 */
public class SpringWebfluxErrorResponseBuilder
    implements ErrorResponseBuilder<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> {

  private final TraceProvider traceProvider;

  /**
   * Constructs a new {@code SpringWebfluxErrorResponseBuilder} with an optional {@link
   * TraceProvider}.
   *
   * @param traceProvider the trace provider to be used for adding trace information to the error
   *     response, may be {@code null}
   */
  public SpringWebfluxErrorResponseBuilder(@Nullable TraceProvider traceProvider) {
    this.traceProvider = traceProvider;
  }

  /** {@inheritDoc} */
  @Override
  public Mono<ResponseEntity<ProblemDetail>> buildResponse(
      final Throwable throwable,
      final ServerWebExchange request,
      final HttpStatus status,
      final HttpHeaders headers,
      final Problem problem) {
    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
      request.getAttributes().put(ERROR_EXCEPTION, throwable);
    }

    ProblemDetail problemDetail = createProblemDetail(request, status, problem, this.traceProvider);
    Optional<Mono<ResponseEntity<ProblemDetail>>> responseEntity =
        negotiate(request)
            .map(
                contentType ->
                    Mono.just(
                        ResponseEntity.status(status)
                            .headers(headers)
                            .contentType(contentType)
                            .body(problemDetail)));

    return responseEntity
        .map(responseEntityMono -> postProcess(responseEntityMono, request))
        .orElseGet(() -> fallback(request, status, headers, problem));
  }

  /**
   * Negotiates the content type for the error response based on the "Accept" header.
   *
   * @param request the current exchange
   * @return an optional media type for the problem response
   */
  Optional<MediaType> negotiate(final ServerWebExchange request) {
    final List<MediaType> mediaTypes = new HeaderContentTypeResolver().resolveMediaTypes(request);
    return ErrorResponseBuilder.getProblemMediaType(mediaTypes);
  }

  /**
   * Post-processes the error response. This implementation simply returns the response as-is.
   *
   * @param errorResponse the error response mono
   * @param request the current exchange
   * @return the processed response mono
   */
  private Mono<ResponseEntity<ProblemDetail>> postProcess(
      final Mono<ResponseEntity<ProblemDetail>> errorResponse, final ServerWebExchange request) {
    return errorResponse;
  }

  /**
   * Fallback method to create an error response if content negotiation fails. It uses {@link
   * MediaTypes#PROBLEM} as the default media type.
   *
   * @param request the current exchange
   * @param status the HTTP status
   * @param headers the HTTP headers
   * @param problem the problem
   * @return the fallback response mono
   */
  private Mono<ResponseEntity<ProblemDetail>> fallback(
      final ServerWebExchange request,
      final HttpStatus status,
      final HttpHeaders headers,
      final Problem problem) {
    ProblemDetail problemDetail = createProblemDetail(request, status, problem, this.traceProvider);
    return Mono.just(
        ResponseEntity.status(status)
            .headers(headers)
            .contentType(MediaTypes.PROBLEM)
            .body(problemDetail));
  }

  /** {@inheritDoc} */
  @Override
  public URI requestUri(final ServerWebExchange request) {
    return URI.create(request.getRequest().getPath().toString());
  }

  /** {@inheritDoc} */
  @Override
  public HttpMethod requestMethod(final ServerWebExchange request) {
    return request.getRequest().getMethod();
  }
}
