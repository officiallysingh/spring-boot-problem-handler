package com.ksoot.problem.spring.advice.http;

import com.ksoot.problem.core.GeneralErrorKey;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.NotAcceptableStatusException;

/**
 * @see NotAcceptableStatusException
 * @see HttpStatus#NOT_ACCEPTABLE
 */
public interface NotAcceptableStatusAdviceTrait<T, R>
		extends BaseNotAcceptableAdviceTrait<T, R> {

	@ExceptionHandler
	default R handleMediaTypeNotAcceptable(final NotAcceptableStatusException exception,
			final T request) {
		return toProblem(exception, request, GeneralErrorKey.INTERNAL_SERVER_ERROR,
				HttpStatus.NOT_ACCEPTABLE);
	}
}
