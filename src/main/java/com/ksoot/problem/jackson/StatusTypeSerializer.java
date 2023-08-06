package com.ksoot.problem.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.http.HttpStatusCode;

import java.io.IOException;

final class StatusTypeSerializer extends JsonSerializer<HttpStatusCode> {

  @Override
  public void serialize(final HttpStatusCode status, final JsonGenerator json, final SerializerProvider serializers) throws IOException {
    json.writeNumber(status.value());
  }
}
