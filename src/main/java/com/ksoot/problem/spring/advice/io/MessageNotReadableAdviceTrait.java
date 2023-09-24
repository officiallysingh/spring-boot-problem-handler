package com.ksoot.problem.spring.advice.io;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.config.ProblemMessageSourceResolver;
import com.ksoot.problem.core.GeneralErrorKey;
import com.ksoot.problem.core.Problem;
import com.ksoot.problem.core.ProblemConstant;
import org.apache.commons.lang3.ClassUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @see HttpMessageNotReadableException
 * @see HttpStatus#BAD_REQUEST
 */
public interface MessageNotReadableAdviceTrait<T, R> extends AdviceTrait<T, R> {

	@ExceptionHandler
	default R handleMessageNotReadableException(
			final HttpMessageNotReadableException exception, final T request) {
		if (exception
				.getCause() instanceof InvalidFormatException invalidFormatException) {
			return handleInvalidFormatException(invalidFormatException, request);
		}
		return create(exception, request, HttpStatus.BAD_REQUEST);
	}

	default R handleInvalidFormatException(
			final InvalidFormatException invalidFormatException, final T request) {

		String[] errorPropertyKeys = deriveInvalidFormatExceptionErrorKeys(
				invalidFormatException);

		String[] codeCodes = Arrays.stream(errorPropertyKeys)
				.map(errorKey -> ProblemConstant.CODE_CODE_PREFIX + errorKey)
				.toArray(String[]::new);
		String[] titleCodes = Arrays.stream(errorPropertyKeys)
				.map(errorKey -> ProblemConstant.TITLE_CODE_PREFIX + errorKey)
				.toArray(String[]::new);
		String[] detailCodes = Arrays.stream(errorPropertyKeys)
				.map(errorKey -> ProblemConstant.DETAIL_CODE_PREFIX + errorKey)
				.toArray(String[]::new);

		Problem problem = toProblem(invalidFormatException,
				ProblemMessageSourceResolver.of(codeCodes,
						"" + HttpStatus.BAD_REQUEST.value()),
				ProblemMessageSourceResolver.of(titleCodes,
						HttpStatus.BAD_REQUEST.getReasonPhrase()),
				ProblemMessageSourceResolver.of(detailCodes,
						invalidFormatException.getMessage()));

		return create(invalidFormatException, request, HttpStatus.BAD_REQUEST, problem);
	}

	default String[] deriveInvalidFormatExceptionErrorKeys(
			final InvalidFormatException invalidFormatException) {
		List<Reference> pathRef = invalidFormatException.getPath();
		if (isNotEmpty(pathRef)) {
			String desc = pathRef.get(0).getDescription();
			String packageName = desc.contains("[")
					? desc.substring(0, desc.lastIndexOf("."))
					: desc;
			List<String> classes = pathRef.stream().map(Reference::getDescription)
					.filter(description -> description.contains("[\""))
					.map(description -> description.substring(
							description.lastIndexOf(".") + 1,
							description.lastIndexOf("[")))
					.toList();
			String classNames = String.join(".", classes);
			Reference ref = pathRef.get(pathRef.size() - 1);
			String fieldName = ref.getFieldName();
			String targetType = ClassUtils
					.getName(invalidFormatException.getTargetType());

			return new String[] {
					GeneralErrorKey.INVALID_FORMAT + ProblemConstant.DOT + packageName
							+ ProblemConstant.DOT + classNames + ProblemConstant.DOT
							+ fieldName,
					GeneralErrorKey.INVALID_FORMAT + ProblemConstant.DOT + classNames
							+ ProblemConstant.DOT + fieldName,
					GeneralErrorKey.INVALID_FORMAT + ProblemConstant.DOT + targetType
							+ ProblemConstant.DOT + fieldName,
					GeneralErrorKey.INVALID_FORMAT + ProblemConstant.DOT + fieldName };
		}
		else {
			return new String[] { GeneralErrorKey.INVALID_FORMAT };
		}
	}
}
