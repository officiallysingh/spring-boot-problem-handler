package com.ksoot.problem;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.ksoot.problem.core.ApplicationException;
import com.ksoot.problem.core.ApplicationProblem;
import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.MultiProblem;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import com.ksoot.problem.core.ProblemUtils;
import com.ksoot.problem.core.ThrowableProblem;
import com.ksoot.problem.spring.config.ProblemBeanRegistry;
import com.ksoot.problem.spring.config.ProblemMessageProvider;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import jakarta.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.experimental.UtilityClass;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

@UtilityClass
public class Problems {

  // ------------------------------- Factory methods
  // -------------------------------------

  public static CauseBuilder newInstance(
      final String code, final String title, final String detail) {
    return new Builder(code, title, detail);
  }

  public static CauseBuilder newInstance(
      final MessageSourceResolvable codeResolver,
      final MessageSourceResolvable titleResolver,
      final MessageSourceResolvable detailResolver) {
    return new Builder(codeResolver, titleResolver, detailResolver);
  }

  public static DefaultDetailBuilder newInstance(final String errorKey) {
    return new Builder(errorKey);
  }

  public static TitleBuilder newInstance(final MessageSourceResolvable codeResolver) {
    return new Builder(codeResolver);
  }

  // -------- Utility methods to prepare throwables from existing problem instances
  // -----------
  public static ApplicationProblem throwAble(final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return new ApplicationProblem(
        status,
        problem.getCode(),
        problem.getTitle(),
        problem.getDetail(),
        problem.getCause(),
        problem.getParameters());
  }

  public static MultiProblem throwAble(final HttpStatus status, final List<Problem> problems) {
    return MultiProblem.of(status, problems);
  }

  public static MultiProblem throwAble(final HttpStatus status, final Problem... problems) {
    Assert.notNull(problems, "'problems' must not be null");
    return MultiProblem.of(status, Arrays.asList(problems));
  }

  public static ApplicationException throwAbleChecked(
      final HttpStatus status, final Problem problem) {
    Assert.notNull(problem, "'problem' must not be null");
    return new ApplicationException(
        status,
        problem.getCode(),
        problem.getTitle(),
        problem.getDetail(),
        problem.getCause(),
        problem.getParameters());
  }

  public static ApplicationProblem notFound() {
    return newInstance(GeneralErrorKey.NOT_FOUND).throwAble(NOT_FOUND);
  }

  public static ApplicationProblem internalServerError() {
    return newInstance(GeneralErrorKey.INTERNAL_SERVER_ERROR).throwAble(INTERNAL_SERVER_ERROR);
  }

  // ---------------------- Builder -------------------------
  public interface TitleBuilder {
    public DetailBuilder title(final MessageSourceResolvable titleResolver);
  }

  public interface DetailBuilder {
    public DefaultDetailBuilder detail(final MessageSourceResolvable detailResolver);
  }

  public interface DefaultDetailBuilder extends DetailArgsBuilder {
    public DetailArgsBuilder defaultDetail(@Nullable final String detail);
  }

  public interface DetailArgsBuilder extends CauseBuilder {
    public CauseBuilder detailArgs(@Nullable final Object... detailArgs);
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

  public static class Builder implements TitleBuilder, DetailBuilder, DefaultDetailBuilder {

    private static final Set<String> RESERVED_PROPERTIES =
        new HashSet<>(
            Arrays.asList(
                "code",
                "title",
                "detail",
                "cause",
                ProblemConstant.CODE_RESOLVER,
                ProblemConstant.TITLE_RESOLVER,
                ProblemConstant.DETAIL_RESOLVER));

    private String code;
    private String title;
    private String detail;

    private String errorKey;
    private String defaultDetail;
    private Object[] detailArgs;

    private ThrowableProblem cause;

    private Map<String, Object> parameters = new LinkedHashMap<>(4);

    Builder(final String code, final String title, final String detail) {
      this.code = code;
      this.title = title;
      this.detail = detail;
    }

    Builder(final String errorKey) {
      this.errorKey = errorKey;
    }

    Builder(final MessageSourceResolvable codeResolver) {
      this.code = ProblemMessageProvider.getMessage(codeResolver);
      this.parameters.put("codeResolver", codeResolver);
    }

    Builder(
        final MessageSourceResolvable codeResolver,
        final MessageSourceResolvable titleResolver,
        final MessageSourceResolvable detailResolver) {
      this(
          ProblemMessageProvider.getMessage(codeResolver),
          ProblemMessageProvider.getMessage(titleResolver),
          ProblemMessageProvider.getMessage(detailResolver));

      if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
        this.parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
        this.parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
        this.parameters.put(ProblemConstant.DETAIL_RESOLVER, detailResolver);
      }
    }

    @Override
    public DetailBuilder title(final MessageSourceResolvable titleResolver) {
      Assert.notNull(titleResolver, "'titleResolver' must not be null");
      this.title = ProblemMessageProvider.getMessage(titleResolver);
      if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
        this.parameters.put("titleResolver", titleResolver);
      }
      return this;
    }

    @Override
    public DefaultDetailBuilder detail(final MessageSourceResolvable detailResolver) {
      Assert.notNull(detailResolver, "'detailResolver' must not be null");
      this.detail = ProblemMessageProvider.getMessage(detailResolver);
      if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
        this.parameters.put("messageResolver", detailResolver);
      }
      return this;
    }

    @Override
    public DetailArgsBuilder defaultDetail(@Nullable final String detail) {
      this.defaultDetail = detail;
      return this;
    }

    @Override
    public CauseBuilder detailArgs(@Nullable final Object... detailArgs) {
      this.detailArgs = detailArgs;
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
        parameters.entrySet().stream()
            .forEach(entry -> parameter(entry.getKey(), entry.getValue()));
      }
      return this;
    }

    private void setParamsByErrorKey() {
      if (StringUtils.isNotEmpty(this.errorKey)) {
        MessageSourceResolvable codeResolver =
            ProblemMessageSourceResolver.of(ProblemConstant.CODE_CODE_PREFIX + this.errorKey);
        MessageSourceResolvable titleResolver =
            ProblemMessageSourceResolver.of(ProblemConstant.TITLE_CODE_PREFIX + this.errorKey);
        MessageSourceResolvable detailResolver =
            ProblemMessageSourceResolver.of(
                ProblemConstant.DETAIL_CODE_PREFIX + this.errorKey,
                this.defaultDetail,
                this.detailArgs);

        this.code = ProblemMessageProvider.getMessage(codeResolver);
        this.title = ProblemMessageProvider.getMessage(titleResolver);
        this.detail = ProblemMessageProvider.getMessage(detailResolver);

        if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
          this.parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
          this.parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
          this.parameters.put(ProblemConstant.DETAIL_CODE_PREFIX, detailResolver);
        }
      }
    }

    private void setParamsByErrorKey(final HttpStatus status) {
      if (StringUtils.isNotEmpty(this.errorKey)) {
        MessageSourceResolvable codeResolver =
            ProblemMessageSourceResolver.of(
                ProblemConstant.CODE_CODE_PREFIX + this.errorKey, status.value());
        MessageSourceResolvable titleResolver =
            ProblemMessageSourceResolver.of(
                ProblemConstant.TITLE_CODE_PREFIX + this.errorKey, status.getReasonPhrase());
        MessageSourceResolvable detailResolver =
            ProblemMessageSourceResolver.of(
                ProblemConstant.DETAIL_CODE_PREFIX + this.errorKey,
                this.defaultDetail,
                this.detailArgs);

        this.code = ProblemMessageProvider.getMessage(codeResolver);
        this.title = ProblemMessageProvider.getMessage(titleResolver);
        this.detail = ProblemMessageProvider.getMessage(detailResolver);

        if (ProblemBeanRegistry.problemProperties().isDebugEnabled()) {
          this.parameters.put(ProblemConstant.CODE_RESOLVER, codeResolver);
          this.parameters.put(ProblemConstant.TITLE_RESOLVER, titleResolver);
          this.parameters.put(ProblemConstant.DETAIL_CODE_PREFIX, detailResolver);
        }
      }
    }

    @Override
    public Problem get() {
      this.setParamsByErrorKey();
      return Problem.of(this.code, this.title, this.detail)
          .cause(this.cause)
          .parameters(this.parameters)
          .build();
    }

    @Override
    public ApplicationProblem throwAble(final HttpStatus status) {
      this.setParamsByErrorKey(status);
      return ApplicationProblem.of(
          status, this.code, this.title, this.detail, this.cause, this.parameters);
    }

    @Override
    public ApplicationException throwAbleChecked(final HttpStatus status) {
      this.setParamsByErrorKey(status);
      return ApplicationException.of(
          status, this.code, this.title, this.detail, this.cause, this.parameters);
    }
  }
}
