package com.arabot.store.apigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {


    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("products", r -> r.path("/products/**")
                        .uri("http://localhost:8081")).
                route("users", r -> r.path("/users/**")
                .uri("http://localhost:8082"))
                .build();
    }
}
