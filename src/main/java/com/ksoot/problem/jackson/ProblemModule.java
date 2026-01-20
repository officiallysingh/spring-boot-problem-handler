package com.ksoot.problem.jackson;

import com.ksoot.problem.core.DefaultProblem;
import com.ksoot.problem.core.Exceptional;
import com.ksoot.problem.core.Problem;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import tools.jackson.core.Version;
import tools.jackson.core.util.VersionUtil;
import tools.jackson.databind.JacksonModule;
import tools.jackson.databind.module.SimpleDeserializers;
import tools.jackson.databind.module.SimpleSerializers;
import tools.jackson.databind.ser.std.ToStringSerializer;

/** Jackson module to serialize and deserialize Problem classes. */
public final class ProblemModule extends JacksonModule {

  private final Map<Integer, HttpStatusCode> statuses;

  /**
   * Default constructor that uses {@link HttpStatus} as the status type.
   *
   * @see HttpStatus
   */
  public ProblemModule() {
    this(HttpStatus.class);
  }

  /**
   * Constructs a new problem module with the given status types.
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

  /** {@inheritDoc} */
  @Override
  public String getModuleName() {
    return ProblemModule.class.getSimpleName();
  }

  /** {@inheritDoc} */
  @Override
  public Version version() {
    return VersionUtil.versionFor(ProblemModule.class);
  }

  private Class<?> mixinClass() {
    return ExceptionalMixin.class;
  }

  /** {@inheritDoc} */
  @Override
  public void setupModule(final SetupContext context) {
    context.setMixIn(Exceptional.class, mixinClass());
    context.setMixIn(DefaultProblem.class, AbstractThrowableProblemMixIn.class);
    context.setMixIn(Problem.class, ProblemMixIn.class);

    final SimpleSerializers serializers = new SimpleSerializers();
    serializers.addSerializer(HttpStatusCode.class, new HttpStatusSerializer());
    serializers.addSerializer(HttpMethod.class, new HttpMethodSerializer());
    serializers.addSerializer(
        StackTraceElement.class, new ToStringSerializer(StackTraceElement.class));
    context.addSerializers(serializers);

    final SimpleDeserializers deserializers = new SimpleDeserializers();
    deserializers.addDeserializer(HttpStatusCode.class, new HttpStatusDeserializer(this.statuses));
    deserializers.addDeserializer(HttpMethod.class, new HttpMethodDeserializer());
    context.addDeserializers(deserializers);
  }
}
