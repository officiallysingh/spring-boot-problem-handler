package com.ksoot.problem.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import org.springframework.http.HttpStatusCode;

final class HttpStatusSerializer extends JsonSerializer<HttpStatusCode> {

  @Override
  public void serialize(
      final HttpStatusCode status, final JsonGenerator json, final SerializerProvider serializers)
      throws IOException {
    json.writeNumber(status.value());
  }
}
