package com.backend.blooming.authentication.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                        .filters(exchangeFilterFunctions -> {
                            exchangeFilterFunctions.add(logRequest());
                            exchangeFilterFunctions.add(logResponse());
                        })
                        .build();
    }

    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("REQUEST: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                         .forEach((name, values) -> values.forEach(value -> log.info("{}: {}", name, value)));

            return Mono.just(clientRequest);
        });
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            clientResponse.headers()
                          .asHttpHeaders()
                          .forEach((name, values) -> values.forEach(value -> log.info("{}: {}", name, value)));

            return Mono.just(clientResponse);
        });
    }
}
