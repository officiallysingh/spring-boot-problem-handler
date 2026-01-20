package com.ksoot.problem.jackson;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ksoot.problem.core.DefaultProblem;
import com.ksoot.problem.core.Problem;
import java.util.Map;

/** Jackson MixIn for {@link Problem} to configure JSON serialization. */
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    defaultImpl = DefaultProblem.class,
    visible = true)
@JsonInclude(NON_EMPTY)
@JsonPropertyOrder({"code", "title", "details"})
interface ProblemMixIn extends Problem {

  /** {@inheritDoc} */
  @JsonProperty("code")
  @Override
  String getCode();

  /** {@inheritDoc} */
  @JsonProperty("title")
  @Override
  String getTitle();

  /** {@inheritDoc} */
  @JsonProperty("detail")
  @Override
  String getDetail();

  /** {@inheritDoc} */
  @JsonAnyGetter
  @Override
  Map<String, Object> getParameters();
}
