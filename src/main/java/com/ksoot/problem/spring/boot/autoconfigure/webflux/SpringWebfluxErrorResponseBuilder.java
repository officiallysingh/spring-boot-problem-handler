package com.ksoot.problem.spring.boot.autoconfigure.webflux;

import com.ksoot.problem.core.ErrorResponseBuilder;
import com.ksoot.problem.core.MediaTypes;
import com.ksoot.problem.core.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.accept.HeaderContentTypeResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static jakarta.servlet.RequestDispatcher.ERROR_EXCEPTION;

public class SpringWebfluxErrorResponseBuilder
    implements ErrorResponseBuilder<ServerWebExchange, Mono<ResponseEntity<ProblemDetail>>> {

  @Override
  public Mono<ResponseEntity<ProblemDetail>> buildResponse(final Throwable throwable,
                                                           final ServerWebExchange request, final HttpStatus status, final HttpHeaders headers,
                                                           final Problem problem) {
    if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
      request.getAttributes().put(ERROR_EXCEPTION, throwable);
    }

    ProblemDetail problemDetail = createProblemDetail(request, status, problem);
    Optional<Mono<ResponseEntity<ProblemDetail>>> responseEntity = negotiate(request).map(contentType -> Mono
        .just(ResponseEntity.status(status).headers(headers).contentType(contentType).body(problemDetail)));

    if (responseEntity.isPresent()) {
      return postProcess(responseEntity.get(), request);
    } else {
      return fallback(request, status, headers, problem);
    }
  }

  Optional<MediaType> negotiate(final ServerWebExchange request) {
    final List<MediaType> mediaTypes = new HeaderContentTypeResolver().resolveMediaTypes(request);
    return ErrorResponseBuilder.getProblemMediaType(mediaTypes);
  }

  private Mono<ResponseEntity<ProblemDetail>> fallback(final ServerWebExchange request, final HttpStatus status,
                                                       final HttpHeaders headers, final Problem problem) {
    ProblemDetail problemDetail = createProblemDetail(request, status, problem);
    return Mono.just(ResponseEntity.status(status).headers(headers).contentType(MediaTypes.PROBLEM).body(problemDetail));
  }

  private Mono<ResponseEntity<ProblemDetail>> postProcess(final Mono<ResponseEntity<ProblemDetail>> errorResponse,
                                                          final ServerWebExchange request) {
    return errorResponse;
  }

  @Override
  public URI requestUri(final ServerWebExchange request) {
    return URI.create(request.getRequest().getPath().toString());
  }

  @Override
  public HttpMethod requestMethod(final ServerWebExchange request) {
    return request.getRequest().getMethod();
  }
}