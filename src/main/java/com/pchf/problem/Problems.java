package com.pchf.problem;

import com.pchf.problem.core.ApplicationException;
import com.pchf.problem.core.ApplicationProblem;
import com.pchf.problem.core.GeneralErrorKey;
import com.pchf.problem.core.MultiProblem;
import com.pchf.problem.core.Problem;
import com.pchf.problem.core.ProblemUtils;
import com.pchf.problem.core.ThrowableProblem;
import com.pchf.problem.spring.config.ProblemBeanRegistry;
import com.pchf.problem.spring.config.ProblemMessageSourceResolver;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.pchf.problem.core.ProblemConstant.CODE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.CODE_RESOLVER;
import static com.pchf.problem.core.ProblemConstant.DETAILS_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.DETAILS_RESOLVER;
import static com.pchf.problem.core.ProblemConstant.MESSAGE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.MESSAGE_RESOLVER;
import static com.pchf.problem.core.ProblemConstant.TITLE_CODE_PREFIX;
import static com.pchf.problem.core.ProblemConstant.TITLE_RESOLVER;
import static com.pchf.problem.spring.config.ProblemMessageProvider.getMessage;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class Problems {

  private Problems() {
    throw new IllegalStateException("Problems utility class, not supposed to be instantiated");
  }

  // ------------------------------- Factory methods -------------------------------------

  public static CauseBuilder newInstance(final String code, final String title, final String message, final String details) {
    return new Builder(code, title, message, details);
  }

  public static CauseBuilder newInstance(final MessageSourceResolvable codeResolver,
                                         final MessageSourceResolvable titleResolver,
                                         final MessageSourceResolvable messageResolver,
                                         final MessageSourceResolvable detailsResolver) {
    return new Builder(codeResolver, titleResolver, messageResolver, detailsResolver);
  }

  public static DefaultMessageBuilder newInstance(final String errorKey) {
    return new Builder(errorKey);
  }

  public static TitleBuilder newInstance(final MessageSourceResolvable codeResolver) {
    return new Builder(codeResolver);
  }

  // -------- Utility methods to prepare throwables from existing problem instances -----------
  public static ApplicationProblem throwAble(final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return new ApplicationProblem(status, problem.getCode(), problem.getTitle(),
        problem.getMessage(), problem.getDetails(), problem.getCause(), problem.getParameters());
  }

  public static MultiProblem throwAble(final HttpStatus status, final List<Problem> problems) {
    return MultiProblem.of(status, problems);
  }

  public static MultiProblem throwAble(final HttpStatus status, final Problem... problems) {
    Assert.notNull(problems, "'problems' must not be null");
    return MultiProblem.of(status, Arrays.asList(problems));
  }

  public static ApplicationException throwAbleChecked(final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return new ApplicationException(status, problem.getCode(), problem.getTitle(),
        problem.getMessage(), problem.getDetails(), problem.getCause(), problem.getParameters());
  }

  public static ApplicationProblem notFound() {
    return newInstance(GeneralErrorKey.NOT_FOUND).throwAble(NOT_FOUND);
  }

  public static ApplicationProblem internalServerError() {
    return newInstance(GeneralErrorKey.INTERNAL_SERVER_ERROR).throwAble(INTERNAL_SERVER_ERROR);
  }

  // ---------------------- Builder -------------------------
  public interface TitleBuilder {
    public MessageBuilder title(final MessageSourceResolvable titleResolver);
  }

  public interface MessageBuilder {
    public DetailsBuilder message(final MessageSourceResolvable messageResolver);
  }

  public interface DetailsBuilder {
    public CauseBuilder details(final MessageSourceResolvable detailsResolver);
  }

  public interface DefaultMessageBuilder extends MessageArgsBuilder {
    public MessageArgsBuilder defaultMessage(@Nullable final String message);
  }

  public interface MessageArgsBuilder extends DefaultDetailsBuilder {
    public DefaultDetailsBuilder messageArgs(@Nullable final Object... messageArgs);
  }

  public interface DefaultDetailsBuilder extends DetailsArgsBuilder {
    public DetailsArgsBuilder defaultDetails(@Nullable final String details);
  }

  public interface DetailsArgsBuilder extends CauseBuilder {
    public CauseBuilder detailsArgs(@Nullable final Object... detailsArgs);
  }

  public interface CauseBuilder extends ParameterBuilder {
    public ParameterBuilder cause(@Nullable final Throwable cause);
  }

  public interface ParameterBuilder extends ParametersBuilder {
    ParameterBuilder parameter(final String key, final Object value);
  }

  public interface ParametersBuilder extends ProblemBuildable {
    ProblemBuildable parameters(@Nullable final Map<String, Object> parameters);
  }

  public interface ProblemBuildable {

    public Problem get();

    public ApplicationProblem throwAble(final HttpStatus status);

    public ApplicationException throwAbleChecked(final HttpStatus status);
  }

  public static class Builder implements TitleBuilder, MessageBuilder, DetailsBuilder, DefaultMessageBuilder, CauseBuilder {

    private static final Set<String> RESERVED_PROPERTIES = new HashSet<>(
        Arrays.asList("code", "title", "message", "details", "cause",
            CODE_RESOLVER, TITLE_RESOLVER, MESSAGE_RESOLVER, DETAILS_RESOLVER));

    private String code;
    private String title;
    private String message;
    private String details;

    private String errorKey;
    private String defaultMessage;
    private Object[] messageArgs;
    private String defaultDetails;
    private Object[] detailsArgs;

    private ThrowableProblem cause;

    private Map<String, Object> parameters = new LinkedHashMap<>(4);

    Builder(final String code, final String title, final String message, final String details) {
      this.code = code;
      this.title = title;
      this.message = message;
      this.details = details;
    }

    Builder(final String errorKey) {
      this.errorKey = errorKey;
    }

    Builder(final MessageSourceResolvable codeResolver) {
      this.code = getMessage(codeResolver);
      this.parameters.put("codeResolver", codeResolver);
    }

    Builder(final MessageSourceResolvable codeResolver,
            final MessageSourceResolvable titleResolver,
            final MessageSourceResolvable messageResolver,
            final MessageSourceResolvable detailsResolver) {
      this(getMessage(codeResolver), getMessage(titleResolver), getMessage(messageResolver), getMessage(detailsResolver));

      if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
        this.parameters.put(CODE_RESOLVER, codeResolver);
        this.parameters.put(TITLE_RESOLVER, titleResolver);
        this.parameters.put(MESSAGE_RESOLVER, messageResolver);
        this.parameters.put(DETAILS_RESOLVER, detailsResolver);
      }
    }

    @Override
    public MessageBuilder title(final MessageSourceResolvable titleResolver) {
      Assert.notNull(titleResolver, "'titleResolver' must not be null");
      this.title = getMessage(titleResolver);
      if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
        this.parameters.put("titleResolver", titleResolver);
      }
      return this;
    }

    @Override
    public DetailsBuilder message(final MessageSourceResolvable messageResolver) {
      Assert.notNull(messageResolver, "'messageResolver' must not be null");
      this.message = getMessage(messageResolver);
      if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
        this.parameters.put("messageResolver", messageResolver);
      }
      return this;
    }

    @Override
    public CauseBuilder details(final MessageSourceResolvable detailsResolver) {
      Assert.notNull(detailsResolver, "'detailsResolver' must not be null");
      this.details = getMessage(detailsResolver);
      if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
        this.parameters.put("detailsResolver", detailsResolver);
      }
      return this;
    }

    @Override
    public MessageArgsBuilder defaultMessage(@Nullable final String message) {
      this.defaultMessage = message;
      return this;
    }

    @Override
    public DefaultDetailsBuilder messageArgs(@Nullable final Object... messageArgs) {
      this.messageArgs = messageArgs;
      return this;
    }

    @Override
    public DetailsArgsBuilder defaultDetails(@Nullable final String details) {
      this.defaultDetails = details;
      return this;
    }

    @Override
    public CauseBuilder detailsArgs(@Nullable final Object... detailsArgs) {
      this.detailsArgs = detailsArgs;
      return this;
    }

    @Override
    public ParameterBuilder cause(@Nullable final Throwable cause) {
      this.cause = cause != null ? ProblemUtils.toProblem(cause) : null;
      return this;
    }

    @Override
    public ParameterBuilder parameter(String key, Object value) {
      Assert.hasLength(key, "'key' must not be null or empty");
      Assert.isTrue(!RESERVED_PROPERTIES.contains(key), "Property " + key + " is reserved");
      this.parameters.put(key, value);
      return this;
    }

    @Override
    public ProblemBuildable parameters(@Nullable Map<String, Object> parameters) {
      if (MapUtils.isNotEmpty(parameters)) {
        parameters.entrySet().stream().forEach(entry -> parameter(entry.getKey(), entry.getValue()));
      }
      return this;
    }

    private void setParamsByErrorKey() {
      if (StringUtils.isNotEmpty(this.errorKey)) {
        MessageSourceResolvable codeResolver = ProblemMessageSourceResolver.of(CODE_CODE_PREFIX + this.errorKey);
        MessageSourceResolvable titleResolver = ProblemMessageSourceResolver.of(TITLE_CODE_PREFIX + this.errorKey);
        MessageSourceResolvable messageResolver = ProblemMessageSourceResolver.of(MESSAGE_CODE_PREFIX + this.errorKey, this.defaultMessage, this.messageArgs);
        MessageSourceResolvable detailsResolver = ProblemMessageSourceResolver.of(DETAILS_CODE_PREFIX + this.errorKey, this.defaultDetails, this.detailsArgs);

        this.code = getMessage(codeResolver);
        this.title = getMessage(titleResolver);
        this.message = getMessage(messageResolver);
        this.details = getMessage(detailsResolver);

        if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
          this.parameters.put(CODE_RESOLVER, codeResolver);
          this.parameters.put(TITLE_RESOLVER, titleResolver);
          this.parameters.put(MESSAGE_RESOLVER, messageResolver);
          this.parameters.put(DETAILS_RESOLVER, detailsResolver);
        }
      }
    }

    private void setParamsByErrorKey(final HttpStatus status) {
      if (StringUtils.isNotEmpty(this.errorKey)) {
        MessageSourceResolvable codeResolver = ProblemMessageSourceResolver.of(CODE_CODE_PREFIX + this.errorKey, status.value());
        MessageSourceResolvable titleResolver = ProblemMessageSourceResolver.of(TITLE_CODE_PREFIX + this.errorKey, status.getReasonPhrase());
        MessageSourceResolvable messageResolver = ProblemMessageSourceResolver.of(MESSAGE_CODE_PREFIX + this.errorKey, this.defaultMessage, this.messageArgs);
        MessageSourceResolvable detailsResolver = ProblemMessageSourceResolver.of(DETAILS_CODE_PREFIX + this.errorKey, this.defaultDetails, this.detailsArgs);

        this.code = getMessage(codeResolver);
        this.title = getMessage(titleResolver);
        this.message = getMessage(messageResolver);
        this.details = getMessage(detailsResolver);

        if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
          this.parameters.put(CODE_RESOLVER, codeResolver);
          this.parameters.put(TITLE_RESOLVER, titleResolver);
          this.parameters.put(MESSAGE_RESOLVER, messageResolver);
          this.parameters.put(DETAILS_RESOLVER, detailsResolver);
        }
      }
    }

    @Override
    public Problem get() {
      this.setParamsByErrorKey();
      return Problem.of(this.code, this.title, this.message, this.details).cause(this.cause).parameters(this.parameters).build();
    }

    @Override
    public ApplicationProblem throwAble(final HttpStatus status) {
      this.setParamsByErrorKey(status);
      return ApplicationProblem.of(status, this.code, this.title, this.message, this.details, this.cause, this.parameters);
    }

    @Override
    public ApplicationException throwAbleChecked(final HttpStatus status) {
      this.setParamsByErrorKey(status);
      return ApplicationException.of(status, this.code, this.title, this.message, this.details, this.cause, this.parameters);
    }
  }
}
