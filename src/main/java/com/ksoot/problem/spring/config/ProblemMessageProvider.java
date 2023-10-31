package com.ksoot.problem.spring.config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

/**
 * @author Rajveer Singh
 */
public class ProblemMessageProvider {

  private static MessageSource messageSource;

  public ProblemMessageProvider(final MessageSource messageSource) {
    ProblemMessageProvider.messageSource = messageSource;
  }

  public static String getMessage(final String messageCode, final String defaultMessage) {
    return messageSource.getMessage(messageCode, null, defaultMessage, Locale.getDefault());
  }

  public static String getMessage(
      final String messageCode, final String defaultMessage, final Object... params) {
    return messageSource.getMessage(messageCode, params, defaultMessage, Locale.getDefault());
  }

  public static String getMessage(final MessageSourceResolvable resolvable) {
    return messageSource.getMessage(resolvable, Locale.getDefault());
  }
}
