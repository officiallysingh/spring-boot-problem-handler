package com.pchf.problem.spring.boot.autoconfigure.webflux;

import com.pchf.problem.core.Problem;
import com.pchf.problem.ProblemDetails;
import com.pchf.problem.core.ErrorResponseBuilder;
import com.pchf.problem.core.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.accept.HeaderContentTypeResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;

public class SpringWebfluxErrorResponseBuilder
    implements ErrorResponseBuilder<ServerWebExchange, Mono<ResponseEntity<ProblemDetails>>> {

  @Override
  public Mono<ResponseEntity<ProblemDetails>> buildResponse(final Throwable throwable,
                                                            final ServerWebExchange request, final HttpStatus status, final HttpHeaders headers,
                                                            final Problem... problems) {
    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
      request.getAttributes().put(ERROR_EXCEPTION, throwable);
    }

    ProblemDetails problemDetails = ProblemDetails.of(this.requestUri(request), this.requestMethod(request),
        problems);
    Optional<Mono<ResponseEntity<ProblemDetails>>> responseEntity = negotiate(request).map(contentType -> Mono
        .just(ResponseEntity.status(status).headers(headers).contentType(contentType).body(problemDetails)));
    if (responseEntity.isPresent()) {
      return postProcess(responseEntity.get(), request);
    } else {
      return fallback(request, status, headers, problems);
    }
  }

  Optional<MediaType> negotiate(final ServerWebExchange request) {
    final List<MediaType> mediaTypes = new HeaderContentTypeResolver().resolveMediaTypes(request);
    return ErrorResponseBuilder.getProblemMediaType(mediaTypes);
  }

  private Mono<ResponseEntity<ProblemDetails>> fallback(final ServerWebExchange request, final HttpStatus status,
                                                        final HttpHeaders headers, final Problem... problems) {
    ProblemDetails problemDetails = ProblemDetails.of(this.requestUri(request), this.requestMethod(request),
        problems);
    return Mono.just(ResponseEntity.status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(problemDetails));
  }

  private Mono<ResponseEntity<ProblemDetails>> postProcess(final Mono<ResponseEntity<ProblemDetails>> errorResponse,
                                                           final ServerWebExchange request) {
    return errorResponse;
  }

  private URI requestUri(final ServerWebExchange request) {
    return URI.create(request.getRequest().getPath().toString());
  }

  private HttpMethod requestMethod(final ServerWebExchange request) {
    return request.getRequest().getMethod();
  }
}