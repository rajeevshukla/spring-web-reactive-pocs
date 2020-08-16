package com.developervisits.reactive.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class FluxAndMonoFactoryTest {

    List<String> list = Arrays.asList("Spring", "Core","Test", "Work");
    @Test
    public void createFluxUsingIterable(){
        Flux<String> nameFlux = Flux.fromIterable(list);
        StepVerifier.create(nameFlux)
                .expectNext("Spring", "Core","Test", "Work")
                .verifyComplete();
    }

    @Test
    public void createFluxUsingArray(){
        String [] name = new String[] {"Name1", "name2", "Name4"};
        Flux<String> nameFlux = Flux.fromArray(name);
        StepVerifier.create(nameFlux)
                .expectNext("Name1", "name2", "Name4")
                .verifyComplete();
    }

    @Test
    public void createMono() {
       Mono<String> emptyMono =  Mono.justOrEmpty(null);
       StepVerifier.create(emptyMono)   // empty mono is not going to emit anything hence call verifycomplete.
               .verifyComplete();
    }

    @Test
    public void createMonoUsingSupplier () {
        Supplier<String> stringSupplier = ()-> "Reactive";
        Mono<String> emptyMono =  Mono.fromSupplier(stringSupplier);
        StepVerifier.create(emptyMono)
                .expectNext("Reactive") // empty mono is not going to emit anything hence call verifycomplete.
                .verifyComplete();
    }
    @Test
    public void creatFluxUsingRange() {
        Flux<Integer> integerFlux = Flux.range(1,5);
        StepVerifier.create(integerFlux.log())
                .expectNextCount(5)
                .verifyComplete();
    }


}
