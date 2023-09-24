package com.ksoot.problem.spring.advice;

import com.ksoot.problem.core.ThrowableProblem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

/**
 * Spring version of {@link BaseAdviceTrait} which uses {@link HttpStatus} rather than
 * {@link HttpStatus}.
 *
 * @see BaseAdviceTrait
 */
public interface SpringAdviceTrait<T, R> extends AdviceTrait<T, R> {

	default R create(final HttpStatus status, final Throwable throwable,
			final T request) {
		return create(status, throwable, request, new HttpHeaders());
	}

	default R create(final HttpStatus status, final Throwable throwable, final T request,
			final HttpHeaders headers) {
		return create(throwable, request, status, headers);
	}

	default ThrowableProblem toProblem(final Throwable throwable,
			final HttpStatus status) {
		return toProblem(throwable, status);
	}
}
