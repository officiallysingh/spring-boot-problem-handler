package com.ksoot.problem.core;

public class ProblemConstant {

  public static final String CODE_KEY = "code";
  public static final String METHOD_KEY = "method";
  public static final String TIMESTAMP_KEY = "timestamp";
  public static final String STACKTRACE_KEY = "statcktrace";
  public static final String CAUSE_KEY = "cause";
  public static final String ERRORS_KEY = "errors";
  public static final String VIOLATIONS_KEY = "violations";
  public static final String DOT = ".";

  public static final String CODE_CODE_PREFIX = "code.";
  public static final String TITLE_CODE_PREFIX = "title.";
  public static final String DETAIL_CODE_PREFIX = "detail.";
  public static final String STATUS_CODE_PREFIX = "status.";

  public static final String CODE_RESOLVER = "codeResolver";
  public static final String TITLE_RESOLVER = "titleResolver";
  public static final String DETAIL_RESOLVER = "detailResolver";

  public static final String STATUS_RESOLVER = "statusResolver";

  public static final String PROPERTY_PATH_KEY = "propertyPath";

  public static final String CONSTRAINT_VIOLATION_CODE_CODE_PREFIX =
      CODE_CODE_PREFIX + GeneralErrorKey.CONSTRAINT_VIOLATION;
  public static final String CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX =
      TITLE_CODE_PREFIX + GeneralErrorKey.CONSTRAINT_VIOLATION;
  public static final String CONSTRAINT_VIOLATION_DETAIL_CODE_PREFIX =
      DETAIL_CODE_PREFIX + GeneralErrorKey.CONSTRAINT_VIOLATION;

  public static final String CONSTRAINT_VIOLATION_DEFAULT_MESSAGE = "Constraints violated";
  public static final String DB_CONSTRAINT_VIOLATION_DEFAULT_MESSAGE =
      "Database constraints violated";

  private ProblemConstant() {
    throw new IllegalStateException("Just a constants container, not supposed to be instantiated");
  }
}
