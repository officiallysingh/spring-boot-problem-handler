package com.ksoot.problem.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

import java.util.Map;

@SuppressWarnings("serial")
public final class ApplicationProblem extends ThrowableProblem {

	private final HttpStatus status;

	private final String code;

	private final String title;

	private final String detail;

	private Map<String, Object> parameters;

	public ApplicationProblem(final HttpStatus status, final String code,
			final String title, final String detail,
			@Nullable final ThrowableProblem cause,
			@Nullable final Map<String, Object> parameters) {
		super(cause);
		Assert.notNull(status, "'status' must not be null");
		Assert.hasText(code, "'code' must not be null or empty");
		Assert.hasText(title, "'title' must not be null or empty");
		Assert.hasText(detail, "'detail' must not be null or empty");
		this.status = status;
		this.code = code;
		this.title = title;
		this.detail = detail;
		this.parameters = parameters;
	}

	public static ApplicationProblem of(final HttpStatus status, final String code,
			final String title, final String detail) {
		return new ApplicationProblem(status, code, title, detail, null, null);
	}

	public static ApplicationProblem of(final HttpStatus status, final String code,
			final String title, final String detail,
			@Nullable final ThrowableProblem cause) {
		return new ApplicationProblem(status, code, title, detail, cause, null);
	}

	public static ApplicationProblem of(final HttpStatus status, final String code,
			final String title, final String detail,
			@Nullable final Map<String, Object> parameters) {
		return new ApplicationProblem(status, code, title, detail, null, parameters);
	}

	public static ApplicationProblem of(final HttpStatus status, final String code,
			final String title, final String detail,
			@Nullable final ThrowableProblem cause,
			@Nullable final Map<String, Object> parameters) {
		return new ApplicationProblem(status, code, title, detail, cause, parameters);
	}

	@JsonIgnore
	public HttpStatus status() {
		return this.status;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getDetail() {
		return this.detail;
	}

	@Override
	public Map<String, Object> getParameters() {
		return this.parameters;
	}
}
