package com.ksoot.problem.core;

/** Constants for general error keys. */
public class GeneralErrorKey {

  public static final String INTERNAL_SERVER_ERROR = "internal.server.error";

  public static final String DATA_INTEGRITY_VIOLATION = "data.integrity.violation";

  public static final String CONSTRAINT_VIOLATION = "constraint.violation";

  public static final String MISSING_SERVLET_REQUEST_PARAMETER = "missing.request.parameter";

  public static final String MISSING_SERVLET_REQUEST_PART = "missing.request.part";

  public static final String NO_HANDLER_FOUND = "no.handler.found";

  public static final String MISSING_REQUEST_HEADER = "missing.request.header";

  public static final String OPEN_API_VIOLATION = "openapi.violation";

  public static final String TYPE_MISMATCH = "type.mismatch";

  public static final String INVALID_FORMAT = "invalid.format";

  public static final String NOT_FOUND = "not.found";

  public static final String REQUEST_METHOD_NOT_SUPPORTED = "request.method.not.supported";

  public static final String METHOD_NOT_ALLOWED = "method.not.allowed";

  public static final String MEDIA_TYPE_NOT_ACCEPTABLE = "media.type.not.acceptable";

  public static final String MEDIA_TYPE_NOT_SUPPORTED = "media.type.not.supported";

  public static final String SECURITY_UNAUTHORIZED = "security.unauthorized";

  public static final String SECURITY_ACCESS_DENIED = "security.access.denied";

  public static final String MULTIPLE_ERRORS = "multiple.errors";

  private GeneralErrorKey() {
    throw new IllegalStateException("Just a constants container, not supposed to be instantiated");
  }
}
