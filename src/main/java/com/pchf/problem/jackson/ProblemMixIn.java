package com.pchf.problem.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.pchf.problem.core.DefaultProblem;
import com.pchf.problem.core.Problem;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_EMPTY;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    defaultImpl = DefaultProblem.class,
    visible = true)
@JsonInclude(NON_EMPTY)
@JsonPropertyOrder({"code", "title", "message", "details"})
interface ProblemMixIn extends Problem {

  @JsonProperty("code")
  @Override
  String getCode();

  @JsonProperty("title")
  @Override
  String getTitle();

  @JsonProperty("detail")
  @Override
  String getDetail();

  @JsonAnyGetter
  @Override
  Map<String, Object> getParameters();
}
