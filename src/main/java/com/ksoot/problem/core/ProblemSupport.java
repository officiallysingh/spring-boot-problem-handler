package com.ksoot.problem.core;

import java.util.Map;
import org.springframework.http.HttpStatus;

/** Interface for providing problem details from an exception or other object. */
public interface ProblemSupport {

  /**
   * Returns the HTTP status associated with the problem.
   *
   * @return the HTTP status
   */
  HttpStatus getStatus();

  /**
   * Returns the error key, used for mapping to a localized message.
   *
   * @return the error key
   */
  String getErrorKey();

  /**
   * Returns the underlying problem.
   *
   * @return the problem
   */
  Problem getProblem();

  /**
   * Returns the default detail message.
   *
   * @return the default detail
   */
  String getDefaultDetail();

  /**
   * Returns the arguments for the localized detail message.
   *
   * @return the detail arguments
   */
  Object[] getDetailArgs();

  /**
   * Returns the cause of the problem.
   *
   * @return the cause
   */
  ThrowableProblem getCause();

  /**
   * Returns additional parameters for the problem.
   *
   * @return additional parameters
   */
  Map<String, Object> getParameters();
}
