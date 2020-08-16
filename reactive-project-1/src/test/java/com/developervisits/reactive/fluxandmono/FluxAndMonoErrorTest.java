package com.developervisits.reactive.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling() {
        Flux<String> stringFlux =Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorResume((e)->{   // handling reactive stream error
                    e.printStackTrace();
                    return Flux.just("Value");
                });

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                //.expectError(RuntimeException.class)
                //.verify();
        .expectNext("Value")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorReturn() {
        Flux<String> stringFlux =Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("Default");

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                //.expectError(RuntimeException.class)
                //.verify();
                .expectNext("Default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorMap() {
        Flux<String> stringFlux =Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e-> new CustomExeption("Test"));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                //.expectError(RuntimeException.class)
                //.verify();
                .expectError(CustomExeption.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_withRetry() {
        Flux<String> stringFlux =Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e-> new CustomExeption("Test"))
                .retry(2);

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                .expectNext("A","B","C") // retry 1
                .expectNext("A","B","C") // retry 2
                //.expectError(RuntimeException.class)
                //.verify();
                .expectError(CustomExeption.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_withRetryBackOff() {
        Flux<String> stringFlux =Flux.just("A","B","C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorMap(e-> new CustomExeption("Test"))
                .retryBackoff(3, Duration.ofSeconds(5));

        StepVerifier.create(stringFlux.log())
                .expectSubscription()
                .expectNext("A","B","C")
                .expectNext("A","B","C") // retry 1
                .expectNext("A","B","C") // retry 2
                //.expectError(RuntimeException.class)
                //.verify();
                .expectError(CustomExeption.class)
                .verify();
    }


}

