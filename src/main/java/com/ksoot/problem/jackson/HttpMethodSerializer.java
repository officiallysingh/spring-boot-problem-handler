package com.ksoot.problem.jackson;

import org.springframework.http.HttpMethod;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

/** Jackson serializer for {@link HttpMethod}. */
final class HttpMethodSerializer extends StdSerializer<HttpMethod> {

  /** Constructs a new {@link HttpMethod} serializer. */
  HttpMethodSerializer() {
    super(HttpMethod.class);
  }

  /** {@inheritDoc} */
  @Override
  public void serialize(HttpMethod method, JsonGenerator json, SerializationContext provider)
      throws JacksonException {
    if (method == null) {
      json.writeNull();
    } else {
      json.writeString(method.name());
    }
  }
}
