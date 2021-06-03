package com.jamp.controllers;

import com.jamp.handler.SportHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Slf4j
@Configuration
public class SportsRouter {

    @Bean
    @Profile("REST")
    public RouterFunction<ServerResponse> routeRest(SportHandler handler) {
        return RouterFunctions
                .route(GET("/sports").and(accept(MediaType.APPLICATION_JSON)), handler::getAllSports)
                .andRoute(POST("/sports").and(accept(MediaType.APPLICATION_JSON)), handler::createSport)
                .andRoute(GET("/sports/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getSport)
                .andRoute(PUT("/sports/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::updateSport)
                .andRoute(DELETE("/sports/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::deleteSport)
                .andRoute(DELETE("/sports").and(accept(MediaType.APPLICATION_JSON)), handler::deleteAllSports);
    }

    @Bean
    @Profile("Task")
    public RouterFunction<ServerResponse> route(SportHandler handler) {
        return RouterFunctions
                .route(POST("/sports").and(accept(MediaType.APPLICATION_JSON)), handler::createSport)
                .andRoute(GET("/sports/{q}").and(accept(MediaType.APPLICATION_JSON)), handler::getSportByPartialName);
    }
}
