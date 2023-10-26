package com.ksoot.problem.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ksoot.problem.core.DefaultProblem;
import com.ksoot.problem.core.Exceptional;
import com.ksoot.problem.core.Problem;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public final class ProblemModule extends Module {

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
   * @param <E> generic enum type
   * @param types status type enums
   * @throws IllegalArgumentException if there are duplicate status codes across all status types
   */
  @SafeVarargs
  public <E extends Enum<?> & HttpStatusCode> ProblemModule(final Class<? extends E>... types)
      throws IllegalArgumentException {

    this(buildIndex(types));
  }

  private ProblemModule(final Map<Integer, HttpStatusCode> statuses) {
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
    return ExceptionalMixin.class;
  }

  @Override
  public void setupModule(final SetupContext context) {
    final SimpleModule module = new SimpleModule();

    module.setMixInAnnotation(Exceptional.class, mixinClass());

    module.setMixInAnnotation(DefaultProblem.class, AbstractThrowableProblemMixIn.class);
    module.setMixInAnnotation(Problem.class, ProblemMixIn.class);

    module.addSerializer(HttpStatusCode.class, new HttpStatusSerializer());
    module.addDeserializer(HttpStatusCode.class, new HttpStatusDeserializer(this.statuses));

    module.addSerializer(HttpMethod.class, new HttpMethodSerializer());
    module.addDeserializer(HttpMethod.class, new HttpMethodDeserializer());

    module.addSerializer(StackTraceElement.class, new ToStringSerializer());

    module.setupModule(context);
  }
}
