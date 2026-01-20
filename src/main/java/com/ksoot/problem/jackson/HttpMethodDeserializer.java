package com.ksoot.problem.jackson;

import org.springframework.http.HttpMethod;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

/** Jackson deserializer for {@link HttpMethod}. */
final class HttpMethodDeserializer extends StdDeserializer<HttpMethod> {

  /** Constructs a new {@link HttpMethod} deserializer. */
  HttpMethodDeserializer() {
    super(HttpMethod.class);
  }

  /** {@inheritDoc} */
  @Override
  public HttpMethod deserialize(final JsonParser json, final DeserializationContext context) {
    final String method = json.getValueAsString();
    if (method == null) {
      return null;
    } else {
      return HttpMethod.valueOf(method);
    }
  }
}
