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
 * {@link ProblemHandlingWebflux} is a composite {@link AdviceTrait} that combines all general
 * built-in advice traits for WebFlux applications into a single interface that makes it easier to
 * use:
 *
 * <pre>{@code
 * @ControllerAdvice
 * public class WebFluxExceptionHandler implements ProblemHandlingWebflux<Mono<ResponseEntity<ProblemDetail>>> {}
 * }</pre>
 *
 * <strong>Note:</strong> Future versions of this class will be extended with additional traits.
 *
 * @param <R> the response type
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
