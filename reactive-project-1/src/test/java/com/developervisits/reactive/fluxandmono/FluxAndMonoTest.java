package com.developervisits.reactive.fluxandmono;


import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxAndMonoTest {

    @Test
    public void fluxTest() {
        Flux<String> fluxOfString = Flux.just("Spring", "Spring boot", "Spring Reactive")
                .concatWith(Flux.error(new RuntimeException("Exception occured")))
                .concatWith(Flux.just("Another one after error which should be sent because exception occurred"))
                .log();
        fluxOfString.subscribe(System.out::println,
                throwable -> throwable.printStackTrace(),
                () -> System.out.println("This is completed block"));
    }

    @Test
    public void fluxTestElements_WithoutError() {
        Flux<String> fluxOfString = Flux.just("Spring", "Spring boot", "Spring Reactive").log();
        System.out.println("printing");
        StepVerifier.create(fluxOfString)
        .expectNext("Spring")
        .expectNext("Spring Boot")
        .expectNext("Spring Reactive")
        .verifyComplete(); // asserting  which is terminal operation  equivalent to subscriber otherwise
        // no event will flow from flux stream
    }

    @Test
    public void fluxTestElements_WithError() {
        Flux<String> fluxOfString = Flux.just("Spring", "Spring boot", "Spring Reactive").
                concatWith(Flux.error(new RuntimeException("Exception")))
                .log();
        System.out.println("printing");
        // no event will flow from flux stream
        StepVerifier.create(fluxOfString)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Spring Reactive")
                .expectErrorMessage("Exception")
                /*.expectError(RuntimeException.class)*/

                .verify();
    }


    @Test
    public void fluxTestElementCount_WithError() {
        Flux<String> fluxOfString = Flux.just("Spring", "Spring boot", "Spring Reactive").
                concatWith(Flux.error(new RuntimeException("Exception")))
                .log();
        System.out.println("printing");
        // no event will flow from flux stream
        StepVerifier.create(fluxOfString)
                .expectNextCount(3)
                .expectErrorMessage("Exception")
                /*.expectError(RuntimeException.class)*/

                .verify();
    }

    @Test
    public void fluxTestElementsArgs_WithError() {
        Flux<String> fluxOfString = Flux.just("Spring", "Spring boot", "Spring Reactive").
                concatWith(Flux.error(new RuntimeException("Exception")))
                .log();
        System.out.println("printing");
        // no event will flow from flux stream
        StepVerifier.create(fluxOfString)
                .expectNext("Spring", "Spring boot", "Spring Reactive")
                .expectErrorMessage("Exception")
                /*.expectError(RuntimeException.class)*/

                .verify();
    }


    @Test
    public void testMono () {
        Mono<String> stringMono = Mono.just("Mono Element").log();
         StepVerifier.create(stringMono)
                 .expectNext("Mono Element")
                 .verifyComplete();
    }


    @Test
    public void testMono_WithError () {
//        Mono<String> stringMono = Mono.just("Mono Element").log();
        StepVerifier.create(Mono.error(new RuntimeException("Error")).log())
                .expectError(RuntimeException.class)
                .verify();
    }



}
