package com.developervisits.reactive.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoCombinedTest {

    @Test
    public void combineUsingMerge() {
        Flux<String> stringFlux = Flux.just("A","B","C");
        Flux<String> stringFlux2 = Flux.just("D","E","F");

        Flux<String> mergedFlux = Flux.merge(stringFlux,stringFlux2);

        StepVerifier.create(mergedFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_ExpectSubscribtion() {
        Flux<String> stringFlux = Flux.just("A","B","C");
        Flux<String> stringFlux2 = Flux.just("D","E","F");

        Flux<String> mergedFlux = Flux.merge(stringFlux,stringFlux2);

        StepVerifier.create(mergedFlux.log())
                .expectSubscription()
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_concate() {
        //every element is going to be delay by 1 second
        Flux<String> stringFlux = Flux.just("A","B","C");
        Flux<String> stringFlux2 = Flux.just("D","E","F");

        Flux<String> mergedFlux = Flux.concat(stringFlux,stringFlux2);

        StepVerifier.create(mergedFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay() {
        //every element is going to be delay by 1 second
        Flux<String> stringFlux = Flux.just("A","B","C").delayElements(Duration.ofSeconds(1));
        Flux<String> stringFlux2 = Flux.just("D","E","F").delayElements(Duration.ofSeconds(1));

        Flux<String> mergedFlux = Flux.merge(stringFlux,stringFlux2);

        StepVerifier.create(mergedFlux)
                .expectNextCount(6)
                .verifyComplete();
    }


    @Test
    public void combineUsingZip() {
        // a different way of merging
        //every element is going to be delay by 1 second
        Flux<String> stringFlux = Flux.just("A","B","C");
        Flux<String> stringFlux2 = Flux.just("D","E","F");

        Flux<String> mergedFlux = Flux.zip(stringFlux,stringFlux2, (t1, t2)->{
            return t1.concat(t2);
        });

        StepVerifier.create(mergedFlux)
                .expectNext("AD","BE","CF")
                .verifyComplete();
    }
}
