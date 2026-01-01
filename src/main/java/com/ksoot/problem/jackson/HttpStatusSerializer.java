package com.ksoot.problem.jackson;

import org.springframework.http.HttpStatusCode;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

final class HttpStatusSerializer extends StdSerializer<HttpStatusCode> {

  HttpStatusSerializer() {
    super(HttpStatusCode.class);
  }

  @Override
  public void serialize(HttpStatusCode status, JsonGenerator json, SerializationContext provider)
      throws JacksonException {
    if (status == null) {
      json.writeNull();
    } else {
      json.writeNumber(status.value());
    }
  }
}
