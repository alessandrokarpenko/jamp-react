package com.jamp.repo;

import com.jamp.model.Sport;
import org.springframework.data.couchbase.repository.Query;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;
import reactor.core.publisher.Flux;

public interface SportRepository extends ReactiveCouchbaseRepository<Sport, String> {

    @Query("#{#n1ql.selectEntity} WHERE lower(name) LIKE lower('%$1%')")
    Flux<Sport> findAllByNameLike(String query);

}
