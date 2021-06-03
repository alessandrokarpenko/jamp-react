package com.jamp.service;

import com.jamp.model.Sport;
import com.jamp.repo.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SportService {

    private final SportRepository sportRepository;

    public Flux<Sport> findAll() {
        return sportRepository.findAll();
    }

    public Mono<Sport> create(Sport person) {
        return sportRepository.existsById(person.getId())
                .flatMap(exists -> exists ? alreadyExists(person.getId()) : sportRepository.save(person));
    }

    public Mono<Sport> find(String id) {
        return sportRepository.findById(id)
                .switchIfEmpty(doesNotExist(id));
    }

    public Flux<Sport> findByPartialName(String q) {
        return sportRepository.findAllByNameLike(q);
    }

    public Mono<Sport> update(String id, Sport person) {
        return sportRepository.existsById(id).flatMap(exists -> exists ?
                sportRepository.save(new Sport(id, person.getDecathlonId(), person.getName())) :
                doesNotExist(id));
    }

    public Mono<Void> delete(String id) {
        return sportRepository.existsById(id)
                .flatMap(exists -> exists ? sportRepository.deleteById(id) : doesNotExist(id));
    }

    public Mono<Void> deleteAll() {
        return sportRepository.deleteAll();
    }

    private <T> Mono<T> doesNotExist(String id) {
        return Mono.error(new RuntimeException("Sport with id " + id + " does not exist"));
    }

    private <T> Mono<T> alreadyExists(String id) {
        return Mono.error(new RuntimeException("Sport with id " + id + " already exists"));
    }
}
