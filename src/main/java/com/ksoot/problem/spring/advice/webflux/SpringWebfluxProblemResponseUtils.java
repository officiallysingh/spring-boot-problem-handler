package com.ksoot.problem.spring.advice.webflux;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public final class SpringWebfluxProblemResponseUtils {

  private SpringWebfluxProblemResponseUtils() {
    throw new IllegalStateException("Just a utility class, not supposed to be instantiated");
  }

  public static <T> Mono<Void> writeResponse(
      final ResponseEntity<T> entity, final ServerWebExchange exchange, final ObjectMapper mapper) {
    final ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(entity.getStatusCode());
    response.getHeaders().addAll(entity.getHeaders());
    try {
      final DataBuffer buffer =
          response.bufferFactory().wrap(mapper.writeValueAsBytes(entity.getBody()));
      return response
          .writeWith(Mono.just(buffer))
          .doOnError(error -> DataBufferUtils.release(buffer));
    } catch (final JsonProcessingException ex) {
      return Mono.error(ex);
    }
  }
}
