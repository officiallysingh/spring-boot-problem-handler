package com.ksoot.problem.core;

import jakarta.annotation.Nullable;

import java.util.Map;

public final class DefaultProblem extends AbstractThrowableProblem {

	private static final long serialVersionUID = -6866968751952328910L;

	// TODO needed for jackson
	DefaultProblem(final String code, final String title, final String detail,
			@Nullable final ThrowableProblem cause) {
		super(code, title, detail, cause);
	}

	DefaultProblem(final String code, final String title, final String detail,
			@Nullable final ThrowableProblem cause,
			@Nullable final Map<String, Object> parameters) {
		super(code, title, detail, cause, parameters);
	}
}
