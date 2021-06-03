package com.jamp.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jamp.model.Sport;
import com.jamp.repo.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProjectSetup {

    private static final String DECATHLON_API = "https://sports.api.decathlon.com/sports";


    private final SportRepository sportRepository;


    private final SportSubscriber sportSubscriber;


    private final Gson gson = new Gson();
    private final WebClient client = WebClient.builder().codecs(
            codecs -> codecs.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build();

    @Bean
    public void initSportsCommon() {

        sportRepository.deleteAll().subscribe();

        client.method(HttpMethod.GET)
                .uri(DECATHLON_API)
                .retrieve()
                .bodyToMono(String.class)
                .flatMapMany(res -> Flux.fromIterable(gson.fromJson(res, JsonObject.class).get("data").getAsJsonArray()))
                .map(JsonElement::getAsJsonObject)
                .map(sport -> Optional.ofNullable(createSport(sport)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .log()
//                .subscribe(s -> {
//                    try {
//                        sportRepository.save(s).subscribe();
//                        System.out.println(s);
//                    } catch (DataIntegrityViolationException e) {
//                        log.info(s.getName() + " exists");
//                    }
//                });
        .subscribe(sportSubscriber);
    }


    private Sport createSport(JsonObject e) {
        var name = e.get("name");
        var id = e.get("decathlon_id");
        if (!name.isJsonNull() && !id.isJsonNull()) {
            return Sport.builder().name(name.getAsString()).decathlonId(id.getAsLong()).build();
        }
        return null;
    }
}
