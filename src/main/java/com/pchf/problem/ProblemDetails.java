package com.pchf.problem;

import com.pchf.problem.core.Problem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.util.Assert;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProblemDetails {

  private URI instance;

  private String method;

  private OffsetDateTime timestamp;

  private Integer errorCount;

  private List<Problem> errors;

  public static ProblemDetails of(final URI instance, final HttpMethod method, final List<Problem> problems) {
    return of(instance, method, OffsetDateTime.now(), problems);
  }

  public static ProblemDetails of(final URI instance, final HttpMethod method, final Problem... problems) {
    Assert.notEmpty(problems, "'problems' must not be null");
    return of(instance, method, OffsetDateTime.now(), Arrays.asList(problems));
  }

  public static ProblemDetails of(final URI instance, final HttpMethod method, final OffsetDateTime timestamp,
                                  final List<Problem> problems) {
    Assert.notNull(instance, "'instance' must not be null");
    Assert.notNull(method, "'method' must not be null");
    Assert.notNull(timestamp, "'timestamp' must not be null");
    Assert.notEmpty(problems, "'problems' must not be null or empty");
    Assert.noNullElements(problems, "'problems' must not not contain null elements");
    return new ProblemDetails(instance, method.name(), timestamp, problems.size(), problems);
  }
}
