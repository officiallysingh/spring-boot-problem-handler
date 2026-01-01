package com.ksoot.problem.jackson;

import org.springframework.http.HttpMethod;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

final class HttpMethodSerializer extends StdSerializer<HttpMethod> {

  HttpMethodSerializer() {
    super(HttpMethod.class);
  }

  @Override
  public void serialize(HttpMethod method, JsonGenerator json, SerializationContext provider)
      throws JacksonException {
    if (method == null) {
      json.writeNull();
    } else {
      json.writeNumber(method.name());
    }
  }
}
