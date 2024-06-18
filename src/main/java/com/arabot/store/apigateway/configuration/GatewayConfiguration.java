package com.arabot.store.apigateway.configuration;

import com.arabot.store.apigateway.filter.EncryptedHeaderFilter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Log4j2
public class GatewayConfiguration {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private EncryptedHeaderFilter encryptedHeaderFilter;

    @Value("${microservice.auth.server}")
    private String auth_server;

    @Value("${microservice.products.api}")
    private String products_api;

    @Value("${microservice.cart.api}")
    private String cart_api;

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("users", r -> r.path("/users/**")
                        .filters(f -> f.filter(encryptedHeaderFilter.apply(new EncryptedHeaderFilter.Config())))
                        .uri(getUrl(auth_server)))
                .route("products", r -> r.path("/products/**")
                        .filters(f -> f.filter(encryptedHeaderFilter.apply(new EncryptedHeaderFilter.Config())))
                        .uri(getUrl(products_api)))
                .route("categories", r -> r.path("/categories/**")
                        .filters(f -> f.filter(encryptedHeaderFilter.apply(new EncryptedHeaderFilter.Config())))
                        .uri(getUrl(products_api)))
                .route("cart", r -> r.path("/cart/**")
                        .filters(f -> f.filter(encryptedHeaderFilter.apply(new EncryptedHeaderFilter.Config())))
                        .uri(getUrl(cart_api)))
                .route("test", r -> r.path("/test/**")
                        .uri("http://localhost:8081"))
                .build();
    }

    private String getUrl(String serviceName) {

        try {

            String url = discoveryClient.getInstances(serviceName).get(0).getUri().toString();
            log.info("getUrl method " + url);
            return url;

        } catch (Exception e) {

            log.info("Exception " + e.getMessage());
            return serviceName;
        }
    }
}