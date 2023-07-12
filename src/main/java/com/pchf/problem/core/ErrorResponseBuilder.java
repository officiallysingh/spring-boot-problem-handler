package com.pchf.problem.core;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public interface ErrorResponseBuilder<T, R> {

  ContentNegotiationStrategy DEFAULT_CONTENT_NEGOTIATION_STRATEGY =
      new FallbackContentNegotiationStrategy(new HeaderContentNegotiationStrategy());

  static Optional<MediaType> getProblemMediaType(final List<MediaType> mediaTypes) {
    for (final MediaType mediaType : mediaTypes) {
      if (mediaType.includes(APPLICATION_JSON) || mediaType.includes(MediaTypes.PROBLEM)) {
        return Optional.of(MediaTypes.PROBLEM);
      } else if (mediaType.includes(MediaTypes.X_PROBLEM)) {
        return Optional.of(MediaTypes.X_PROBLEM);
      }
    }

    return Optional.empty();
  }

  public R buildResponse(final Throwable throwable, final T request, final HttpStatus status,
                         final HttpHeaders headers, final Problem... problems);
}
