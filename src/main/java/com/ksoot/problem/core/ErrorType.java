package com.ksoot.problem.core;

import org.springframework.http.HttpStatus;

public interface ErrorType {

  String getErrorKey();

  String getDefaultDetail();

  HttpStatus getStatus();
}
