package com.jamp.config;

import com.jamp.model.Sport;
import com.jamp.repo.SportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class SportSubscriber implements Subscriber<Sport> {

    private Subscription subscription;
    private static final int LIMIT = 20;
    private int onNextAmount;

    private final SportRepository sportRepository;


    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        log.info("Sport initialization started");
        subscription.request(LIMIT);
    }

    @Override
    public void onNext(Sport item) {
        if (item != null) {
            sportRepository.save(item).subscribe();
        }
        onNextAmount++;
        if (onNextAmount % LIMIT == 0) {
            subscription.request(LIMIT);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable instanceof DataIntegrityViolationException) {
            log.error("Error: %s", throwable);
        }
        onNextAmount++;
        if (onNextAmount % LIMIT == 0) {
            subscription.request(LIMIT);
        }
    }

    @Override
    public void onComplete() {
        log.info("Sport initialization completed");
    }
}
