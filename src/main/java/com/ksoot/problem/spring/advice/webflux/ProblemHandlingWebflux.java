package com.ksoot.problem.spring.advice.webflux;

import com.ksoot.problem.spring.advice.AdviceTrait;
import com.ksoot.problem.spring.advice.application.ApplicationAdviceTraits;
import com.ksoot.problem.spring.advice.general.GeneralAdviceTraits;
import com.ksoot.problem.spring.advice.http.HttpAdviceTraits;
import com.ksoot.problem.spring.advice.io.IOAdviceTraits;
import com.ksoot.problem.spring.advice.network.NetworkAdviceTraits;
import com.ksoot.problem.spring.advice.routing.RoutingAdviceTraits;
import com.ksoot.problem.spring.advice.validation.ValidationAdviceTraits;
import org.springframework.web.server.ServerWebExchange;

/**
 * {@link ProblemHandlingWebflux} is a composite {@link AdviceTrait} that combines all built-in
 * advice traits into a single interface that makes it easier to use:
 *
 * <pre><code>
 * {@literal @}ControllerAdvice
 *  public class ExceptionHandling implements ProblemHandlingWebflux
 * </code></pre>
 *
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
public interface ProblemHandlingWebflux<R>
    extends GeneralAdviceTraits<ServerWebExchange, R>,
        HttpAdviceTraits<ServerWebExchange, R>,
        IOAdviceTraits<ServerWebExchange, R>,
        // NetworkAdviceTraits<ServerWebExchange, R>,
        ValidationAdviceTraits<ServerWebExchange, R>,
        WebExchangeBindAdviceTrait<ServerWebExchange, R>,
        ApplicationAdviceTraits<ServerWebExchange, R> {}
