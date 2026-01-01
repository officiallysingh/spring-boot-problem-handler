package com.ksoot.problem.jackson;

import org.springframework.http.HttpMethod;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

final class HttpMethodDeserializer extends StdDeserializer<HttpMethod> {

  HttpMethodDeserializer() {
    super(HttpMethod.class);
  }

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
