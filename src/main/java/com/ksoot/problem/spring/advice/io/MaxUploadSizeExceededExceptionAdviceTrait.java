package com.ksoot.problem.spring.advice.io;

import com.google.common.base.CharMatcher;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

public interface MaxUploadSizeExceededExceptionAdviceTrait<T, R>
		extends AdviceTrait<T, R> {

	@ExceptionHandler
	default R handleMaxUploadSizeExceededException(
			final MaxUploadSizeExceededException exception, final T request) {
		String errorKey = ClassUtils.getName(exception);
		String defultMessage = exception.getMessage();
		long bytes = exception.getMaxUploadSize();
		if (bytes == -1 && exception
				.getMostSpecificCause() instanceof FileSizeLimitExceededException e) {
			defultMessage = e.getMessage();
			final String byteSizeString = CharMatcher.inRange('0', '9')
					.retainFrom(defultMessage);
			bytes = StringUtils.isNotBlank(byteSizeString)
					? Long.parseLong(byteSizeString)
					: bytes;
		}
		String maxFileSizeAllowed = bytes != -1 ? DataSize.ofBytes(bytes).toString()
				: "UNKNOWN";

		String detailCode = ProblemConstant.DETAIL_CODE_PREFIX + errorKey;

		Problem problem = toProblem(exception, HttpStatus.BAD_REQUEST,
				ProblemMessageSourceResolver.of(detailCode, defultMessage,
						new Object[] { maxFileSizeAllowed }));

		return create(exception, request, HttpStatus.BAD_REQUEST, problem);
	}
}
