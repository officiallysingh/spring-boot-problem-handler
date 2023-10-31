package com.ksoot.problem.core;

import java.util.Map;
import org.springframework.http.HttpStatus;

public interface ProblemSupport {

  HttpStatus getStatus();

  String getErrorKey();

  Problem getProblem();

  String getDefaultDetail();

  Object[] getDetailArgs();

  ThrowableProblem getCause();

  Map<String, Object> getParameters();
}
