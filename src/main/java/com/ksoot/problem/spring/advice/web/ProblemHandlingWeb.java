package com.ksoot.problem.spring.advice.web;

import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.advice.application.ApplicationAdviceTraits;
import com.ksoot.problem.spring.advice.general.GeneralAdviceTraits;
import com.ksoot.problem.spring.advice.http.HttpAdviceTraits;
import com.ksoot.problem.spring.advice.io.IOAdviceTraits;
import com.ksoot.problem.spring.advice.network.NetworkAdviceTraits;
import com.ksoot.problem.spring.advice.routing.RoutingAdviceTraits;
import com.ksoot.problem.spring.advice.validation.ValidationAdviceTraits;
import com.ksoot.problem.spring.boot.autoconfigure.web.WebExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ProblemHandlingWeb} is a composite {@link AdviceTrait} that combines all general built-in
 * advice traits for Servlet web applications into a single interface that makes it easier to use:
 *
 * <pre>{@code
 * @ControllerAdvice
 * public class WebExceptionHandler implements ProblemHandlingWeb<ResponseEntity<ProblemDetail>> {}
 * }</pre>
 *
 * <strong>Note:</strong> Future versions of this class will be extended with additional traits.
 *
 * @param <R> the response type
 * @see WebExceptionHandler
 * @see GeneralAdviceTraits
 * @see HttpAdviceTraits
 * @see IOAdviceTraits
 * @see NetworkAdviceTraits
 * @see RoutingAdviceTraits
 * @see ValidationAdviceTraits
 * @see ApplicationAdviceTraits
 */
public interface ProblemHandlingWeb<R>
    extends GeneralAdviceTraits<NativeWebRequest, R>,
        HttpAdviceTraits<NativeWebRequest, R>,
        IOAdviceTraits<NativeWebRequest, R>,
        // NetworkAdviceTraits<NativeWebRequest, R>,
        RoutingAdviceTraits<NativeWebRequest, R>,
        ValidationAdviceTraits<NativeWebRequest, R>,
        ApplicationAdviceTraits<NativeWebRequest, R> {}
