package com.pchf.problem.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.pchf.problem.core.DefaultProblem;
import com.pchf.problem.core.Exceptional;
import com.pchf.problem.core.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class ProblemModule extends Module {

  private final boolean stackTraces;
  private final boolean cause;
  private final Map<Integer, HttpStatusCode> statuses;

  /**
   * TODO document
   *
   * @see HttpStatus
   */
  public ProblemModule() {
    this(HttpStatus.class);
  }

  /**
   * TODO document
   *
   * @param <E>   generic enum type
   * @param types status type enums
   * @throws IllegalArgumentException if there are duplicate status codes across all status types
   */
  @SafeVarargs
  public <E extends Enum<?> & HttpStatusCode> ProblemModule(final Class<? extends E>... types)
      throws IllegalArgumentException {

    this(false, false, buildIndex(types));
  }

  private ProblemModule(final boolean stackTraces, final boolean cause, final Map<Integer, HttpStatusCode> statuses) {
    this.stackTraces = stackTraces;
    this.cause = cause;
    this.statuses = statuses;
  }

  @SafeVarargs
  private static <E extends Enum<?> & HttpStatusCode> Map<Integer, HttpStatusCode> buildIndex(
      final Class<? extends E>... types) {
    final Map<Integer, HttpStatusCode> index = new HashMap<>();

    for (final Class<? extends E> type : types) {
      for (final E status : type.getEnumConstants()) {
        // Skip depricated status "Checkpoint"
        if (((HttpStatus) status).getReasonPhrase().equalsIgnoreCase("Checkpoint")) {
          continue;
        }
        index.put(status.value(), status);
      }
    }

    return Collections.unmodifiableMap(index);
  }

  @Override
  public String getModuleName() {
    return ProblemModule.class.getSimpleName();
  }

  @Override
  public Version version() {
    return VersionUtil.versionFor(ProblemModule.class);
  }

  private Class<?> mixinClass() {
    if (this.stackTraces && this.cause) {
      return ExceptionalMixin.class;
    } else if (!this.stackTraces && !this.cause) {
      return ExceptionalWithoutStacktraceAndCauseMixin.class;
    } else if (!this.stackTraces) {
      return ExceptionalWithoutStacktraceMixin.class;
    } else { //!this.cause
      return ExceptionalWithoutCauseMixin.class;
    }
  }

  @Override
  public void setupModule(final SetupContext context) {
    final SimpleModule module = new SimpleModule();

    module.setMixInAnnotation(Exceptional.class, mixinClass());

    module.setMixInAnnotation(DefaultProblem.class, AbstractThrowableProblemMixIn.class);
    module.setMixInAnnotation(Problem.class, ProblemMixIn.class);

    module.addSerializer(HttpStatusCode.class, new StatusTypeSerializer());
    module.addDeserializer(HttpStatusCode.class, new StatusTypeDeserializer(this.statuses));

    module.setupModule(context);
  }

//    public ProblemModule withStackTraces() {
//        return withStackTraces(true);
//    }

  public ProblemModule with(final boolean stackTraces, final boolean cause) {
    return new ProblemModule(stackTraces, cause, this.statuses);
  }
}
