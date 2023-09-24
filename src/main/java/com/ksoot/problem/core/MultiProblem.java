package com.ksoot.problem.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class MultiProblem extends RuntimeException {

	private final HttpStatus status;

	private final List<Problem> problems;

	private MultiProblem(final HttpStatus status, final List<Problem> problems) {
		super(status.getReasonPhrase());
		Validate.notNull(status, "'status' must not be null");
		Validate.isTrue(CollectionUtils.isNotEmpty(problems),
				"'problems' must not be null or empty");
		Validate.noNullElements(problems, "'problems' must not contain null elements");
		this.status = status;
		this.problems = Collections.unmodifiableList(problems);
	}

	public static MultiProblem of(final HttpStatus status, final List<Problem> problems) {
		return new MultiProblem(status, problems);
	}

	public static MultiProblem of(final List<Problem> problems) {
		return new MultiProblem(HttpStatus.MULTI_STATUS, problems);
	}

	public HttpStatus getStatus() {
		return this.status;
	}

	public List<Problem> getProblems() {
		return this.problems;
	}
}
