package com.ksoot.problem.spring.config;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Utility class for resolving messages using the Spring {@link MessageSource}. It provides static
 * methods to retrieve localized messages.
 *
 * @author Rajveer Singh
 */
public class ProblemMessageProvider {

  private static MessageSource messageSource;

  /**
   * Constructs a new {@code ProblemMessageProvider} with the given {@link MessageSource}.
   *
   * @param messageSource the {@link MessageSource} to be used for message resolution
   */
  public ProblemMessageProvider(final MessageSource messageSource) {
    ProblemMessageProvider.messageSource = messageSource;
  }

  /**
   * Resolves a message for the given code and default message using the current locale.
   *
   * @param messageCode the code to lookup
   * @param defaultMessage the default message to return if the lookup fails
   * @return the resolved message
   */
  public static String getMessage(final String messageCode, final String defaultMessage) {
    return messageSource.getMessage(
        messageCode, null, defaultMessage, LocaleContextHolder.getLocale());
  }

  /**
   * Resolves a message for the given code, default message, and arguments using the current locale.
   *
   * @param messageCode the code to lookup
   * @param defaultMessage the default message to return if the lookup fails
   * @param params arguments for the message
   * @return the resolved message
   */
  public static String getMessage(
      final String messageCode, final String defaultMessage, final Object... params) {
    return messageSource.getMessage(
        messageCode, params, defaultMessage, LocaleContextHolder.getLocale());
  }

  /**
   * Resolves a message using a {@link MessageSourceResolvable} and the current locale.
   *
   * @param resolvable the resolvable object
   * @return the resolved message
   */
  public static String getMessage(final MessageSourceResolvable resolvable) {
    return messageSource.getMessage(resolvable, LocaleContextHolder.getLocale());
  }
}
