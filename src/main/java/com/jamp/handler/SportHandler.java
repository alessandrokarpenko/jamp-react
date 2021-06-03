package com.jamp.handler;

import com.jamp.model.Sport;
import com.jamp.service.SportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class SportHandler {

    private final SportService sportService;
    private final RequestHandler requestHandler;

    public Mono<ServerResponse> getAllSports(ServerRequest request) {
        return sportService
                .findAll()
                .collectList()
                .flatMap(p -> ServerResponse.ok().syncBody(p));
    }

    public Mono<ServerResponse> createSport(ServerRequest request) {
        return requestHandler.requireValidBody(
                body -> body
                        .flatMap(sportService::create)
                        .flatMap(p -> ServerResponse.created(URI.create("/posts/" + p.getId())).build()), request, Sport.class);
    }

    public Mono<ServerResponse> getSport(ServerRequest request) {
        return sportService
                .find(request.pathVariable("id"))
                .flatMap(p -> ServerResponse.ok().syncBody(p));
    }

    public Mono<ServerResponse> updateSport(ServerRequest request) {
        return requestHandler.requireValidBody(
                body -> body
                        .flatMap(p -> sportService.update(request.pathVariable("id"), p))
                        .flatMap(p -> ServerResponse.ok().build()), request, Sport.class);
    }

    public Mono<ServerResponse> deleteSport(ServerRequest request) {
        return sportService
                .delete(request.pathVariable("id"))
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> deleteAllSports(ServerRequest request) {
        return sportService
                .deleteAll()
                .then(ServerResponse.ok().build());
    }

    public Mono<ServerResponse> getSportByPartialName(ServerRequest request) {
//        return sportService
//                .findByPartialName(request.queryParam("q").orElse(""))
//                .collectList()
//                .flatMap(p -> ServerResponse.ok().syncBody(p));
        return sportService
                .findByPartialName(request.pathVariable("q"))
                .collectList()
                .flatMap(p -> ServerResponse.ok().syncBody(p));
    }
}
