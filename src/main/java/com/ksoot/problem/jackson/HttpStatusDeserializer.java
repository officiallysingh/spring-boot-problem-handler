package com.ksoot.problem.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Map;
import org.springframework.http.HttpStatusCode;

final class HttpStatusDeserializer extends JsonDeserializer<HttpStatusCode> {

  private final Map<Integer, HttpStatusCode> index;

  HttpStatusDeserializer(final Map<Integer, HttpStatusCode> index) {
    this.index = index;
  }

  @Override
  public HttpStatusCode deserialize(final JsonParser json, final DeserializationContext context)
      throws IOException {
    final int statusCode = json.getIntValue();
    final HttpStatusCode status = index.get(statusCode);
    // return status == null ? new DefaultHttpStatusCode(statusCode) : status;
    return status;
  }
}
