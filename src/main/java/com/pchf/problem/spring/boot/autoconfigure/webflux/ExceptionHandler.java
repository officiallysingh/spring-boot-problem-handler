package com.pchf.problem.spring.boot.autoconfigure.webflux;

import com.pchf.problem.spring.advice.webflux.ProblemHandlingWebflux;
import com.pchf.problem.spring.boot.autoconfigure.ProblemProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import reactor.core.publisher.Mono;

@AutoConfiguration
@EnableConfigurationProperties(ProblemProperties.class)
@ConditionalOnProperty(prefix = "problem", name = "enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@ControllerAdvice
@RequiredArgsConstructor
class ExceptionHandler implements ProblemHandlingWebflux<Mono<ResponseEntity<ProblemDetail>>> {

}
