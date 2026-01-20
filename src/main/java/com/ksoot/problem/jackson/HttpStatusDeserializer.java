package com.ksoot.problem.jackson;

import java.util.Map;
import org.springframework.http.HttpStatusCode;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.deser.std.StdDeserializer;

/** Jackson deserializer for {@link HttpStatusCode}. */
final class HttpStatusDeserializer extends StdDeserializer<HttpStatusCode> {

  private final Map<Integer, HttpStatusCode> index;

  /**
   * Constructs a new {@link HttpStatusCode} deserializer.
   *
   * @param index the status code index
   */
  HttpStatusDeserializer(final Map<Integer, HttpStatusCode> index) {
    super(HttpStatusCode.class);
    this.index = index;
  }

  /** {@inheritDoc} */
  @Override
  public HttpStatusCode deserialize(JsonParser json, DeserializationContext ctxt)
      throws JacksonException {
    final int statusCode = json.getIntValue();
    return this.index.get(statusCode);
  }
}
