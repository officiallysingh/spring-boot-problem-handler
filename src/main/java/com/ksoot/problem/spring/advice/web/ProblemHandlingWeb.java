package com.ksoot.problem.spring.advice.web;

import com.ksoot.problem.spring.advice.application.ApplicationAdviceTraits;
import com.ksoot.problem.spring.advice.http.HttpAdviceTraits;
import com.ksoot.problem.spring.advice.validation.ValidationAdviceTraits;
import com.ksoot.problem.spring.advice.BaseAdviceTrait;
import com.ksoot.problem.spring.advice.general.GeneralAdviceTraits;
import com.ksoot.problem.spring.advice.io.IOAdviceTraits;
import com.ksoot.problem.spring.advice.network.NetworkAdviceTraits;
import com.ksoot.problem.spring.advice.routing.RoutingAdviceTraits;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ProblemHandlingWeb} is a composite {@link BaseAdviceTrait} that combines all general built-in advice traits into a single
 * interface that makes it easier to use:
 * <pre><code>
 * {@literal @}ControllerAdvice
 *  public class ExceptionHandling implements ProblemHandlingWeb
 * </code></pre>
 * <strong>Note:</strong> Future versions of this class will be extended with additional traits.
 *
 * @see GeneralAdviceTraits
 * @see HttpAdviceTraits
 * @see IOAdviceTraits
 * @see NetworkAdviceTraits
 * @see RoutingAdviceTraits
 * @see ValidationAdviceTraits
 * @see ApplicationAdviceTraits
 */
public interface ProblemHandlingWeb<R> extends
    GeneralAdviceTraits<NativeWebRequest, R>,
    HttpAdviceTraits<NativeWebRequest, R>,
    IOAdviceTraits<NativeWebRequest, R>,
//        NetworkAdviceTraits<NativeWebRequest, R>,
    RoutingAdviceTraits<NativeWebRequest, R>,
    ValidationAdviceTraits<NativeWebRequest, R>,
    ApplicationAdviceTraits<NativeWebRequest, R> {

}
