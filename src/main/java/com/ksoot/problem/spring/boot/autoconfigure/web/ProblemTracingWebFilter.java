package com.ksoot.problem.spring.boot.autoconfigure.web;

import com.ksoot.problem.spring.boot.autoconfigure.TraceProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class ProblemTracingWebFilter extends OncePerRequestFilter {

  private final TraceProvider traceProvider;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    ImmutablePair<@NotEmpty String, String> trace = this.traceProvider.getTraceId();
    response.setHeader(trace.getKey(), trace.getValue());
    filterChain.doFilter(request, response);
  }
}
