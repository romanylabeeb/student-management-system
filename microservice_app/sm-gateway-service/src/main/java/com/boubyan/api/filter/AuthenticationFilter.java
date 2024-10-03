package com.boubyan.api.filter;


import com.boubyan.api.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RouteValidator validator;

    @Autowired
    private JwtUtil jwtUtil;

    public AuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Check if the request method is OPTIONS
            // Bypass authentication for OPTIONS requests (CORS preflight)
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequest().getMethod().toString())) {
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                return exchange.getResponse().setComplete();
            }

            // Check if the request should be secured
            if (validator.isSecured.test(exchange.getRequest())) {
                // Check if the Authorization header exists
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return unauthorizedResponse(exchange, "Missing authorization header");
                }

                // Extract the token from the Authorization header
                String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    return unauthorizedResponse(exchange, "Invalid authorization header format");
                }

                // Remove the 'Bearer ' prefix from the token
                String token = authHeader.substring(7);

                try {
                    // Validate the JWT token
                    jwtUtil.validateToken(token);
                } catch (Exception e) {
                    System.out.println("Invalid access: " + e.getMessage());
                    return unauthorizedResponse(exchange, "Unauthorized access to the application");
                }
            }

            // If everything is fine, proceed to the next filter
            return chain.filter(exchange);
        };
    }

    private Mono<Void> unauthorizedResponse(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        byte[] bytes = ("{\"error\": \"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    public static class Config {
        // Add any specific configuration here if needed
    }
}
