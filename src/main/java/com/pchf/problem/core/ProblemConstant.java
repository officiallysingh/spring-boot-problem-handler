package com.pchf.problem.core;

public class ProblemConstant {

  public static final String DOT = ".";

  public static final String CODE_CODE_PREFIX = "code.";
  public static final String TITLE_CODE_PREFIX = "title.";
  public static final String MESSAGE_CODE_PREFIX = "message.";
  public static final String DETAILS_CODE_PREFIX = "details.";
  public static final String STATUS_CODE_PREFIX = "status.";


  public static final String CODE_RESOLVER = "codeResolver";
  public static final String TITLE_RESOLVER = "titleResolver";
  public static final String MESSAGE_RESOLVER = "messageResolver";
  public static final String DETAILS_RESOLVER = "detailsResolver";

  public static final String STATUS_RESOLVER = "statusResolver";

  public static final String CONSTRAINT_VIOLATION_CODE_CODE_PREFIX = CODE_CODE_PREFIX + GeneralErrorKey.CONSTRAINT_VIOLATION;
  public static final String CONSTRAINT_VIOLATION_TITLE_CODE_PREFIX = TITLE_CODE_PREFIX + GeneralErrorKey.CONSTRAINT_VIOLATION;
  public static final String CONSTRAINT_VIOLATION_MESSAGE_CODE_PREFIX = MESSAGE_CODE_PREFIX + GeneralErrorKey.CONSTRAINT_VIOLATION;
  public static final String CONSTRAINT_VIOLATION_DETAILS_CODE_PREFIX = DETAILS_CODE_PREFIX + GeneralErrorKey.CONSTRAINT_VIOLATION;

  public static final String DB_CONSTRAINT_VIOLATION_DEFAULT_MESSAGE = "Database constraints violated";

  private ProblemConstant() {
    throw new IllegalStateException("Just a constants container, not supposed to be instantiated");
  }
}
