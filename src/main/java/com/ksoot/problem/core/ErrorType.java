package com.ksoot.problem.core;

import org.springframework.http.HttpStatus;

/**
 * Interface representing an error type with a key, default detail, and HTTP status. Recommended to
 * be implemented as Enum, errorKey would be looked up in configured resource bundles to get
 * externalized error properties.
 */
public interface ErrorType {

  /**
   * Returns the error key. Should be unique for each exception case.
   *
   * @return the error key
   */
  String getErrorKey();

  /**
   * Returns the default detail message.
   *
   * @return the default detail
   */
  String getDefaultDetail();

  /**
   * Returns the HTTP status associated with the error type.
   *
   * @return the HTTP status
   */
  HttpStatus getStatus();
}
