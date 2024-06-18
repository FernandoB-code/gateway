package com.arabot.store.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class EncryptedHeaderFilter extends AbstractGatewayFilterFactory<EncryptedHeaderFilter.Config> {

    @Value("${application.security.header.secret-key}")
    private String encryptionSecretKey;

    public EncryptedHeaderFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String encryptedClaims = exchange.getRequest().getHeaders().getFirst("X-Encrypted-Claims");

            if (encryptedClaims != null) {

               // exchange.getRequest().mutate().headers(headers -> headers.remove("X-Authenticated-Username"));
                 exchange.getRequest().mutate().headers(headers -> headers.remove("X-Encrypted-Claims"));


                ServerWebExchange modifiedExchange = exchange.mutate()
                        .request(r -> r.headers(headers -> headers.add("X-Encrypted-Claims", encryptedClaims)))
                        .build();

                return chain.filter(modifiedExchange);
            } else {
                return chain.filter(exchange);
            }
        };
    }

    public static class Config {

    }
}
