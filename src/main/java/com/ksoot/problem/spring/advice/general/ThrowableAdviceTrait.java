package com.ksoot.problem.spring.advice.general;

import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.ProblemUtils;
import com.ksoot.problem.spring.advice.AdviceTrait;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @see Throwable
 * @see Exception
 */
public interface ThrowableAdviceTrait<T, R> extends AdviceTrait<T, R> {

	@ExceptionHandler
	default R handleThrowable(final Throwable throwable, final T request) {
		HttpStatus status = HttpStatus
				.valueOf(ProblemUtils.resolveStatus(throwable).value());
		return toProblem(throwable, request, GeneralErrorKey.INTERNAL_SERVER_ERROR,
				status);
	}
}
