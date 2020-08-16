package com.developervisits.reactive.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxTransfermationTest {

    List<String> list = Arrays.asList("Spring", "Core","Test", "Work");
    @Test
    public void transformUsingMap() {
        Flux<String> stringFlux = Flux.fromIterable(list);
        stringFlux = stringFlux.map(name->name.toUpperCase()).log();
        StepVerifier.create(stringFlux)
                .expectNext("SPRING", "CORE","TEST", "WORK")
                .verifyComplete();
    }
    @Test
    public void transformUsingMap_length() {
        Flux<String> stringFlux = Flux.fromIterable(list);
        Flux< Integer> intFlux = stringFlux.map(name->name.length()).log();
        StepVerifier.create(intFlux)
                .expectNext(6,4,4,4)
                .verifyComplete();
    }

    @Test
    public void transformUsingMap_repeate() {
        Flux<String> stringFlux = Flux.fromIterable(list);
        Flux< Integer> intFlux = stringFlux.map(name->name.length())
                .repeat(1).log();  // this is called pipeline ,, We can build more complex pipeline
        StepVerifier.create(intFlux)
                .expectNext(6,4,4,4,6,4,4,4)
                .verifyComplete();
    }


    @Test
    public void transformUsingMap_flatMap() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("1","2","3","1","2","3"))
                .flatMap(number->Flux.fromIterable(convertToList(number)));
        // flatMap here expect Flux<String> for every number just like in Java 8 how flatMap flattens the List Of List values

        StepVerifier.create( stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    public List<String> convertToList(String s) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Arrays.asList(s,"newvalue");
    }

    @Test
    public void transformUsingMap_flatMap_parallel() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("1","2","3","1","2","3"))
                .window(2) // Flux<Flux<String>> (1,2), (3,1) // does not maintain order
                .flatMap(number->number.map(this::convertToList)
                        .subscribeOn(Schedulers.parallel()))
                .flatMap(s->Flux.fromIterable(s));
// window operation will wait untill 2 element which will become flux of Flux<String> and then subscribeOn(which takes scheudler.parallel() which sends
        // Everything in a separate thread pool .  which goes from main thread to parallel thread.
        StepVerifier.create( stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }


    @Test
    public void transformUsingMap_flatMap_parallel_mainTainOrder() {
        Flux<String> stringFlux = Flux.fromIterable(Arrays.asList("1","2","3","1","2","3"))
                .window(2) // Flux<Flux<String>> (1,2), (3,1)
                .flatMapSequential(number->number.map(this::convertToList) // to maintain order 1,2 , 3,1 in window you have to
                        // use flatMapSequential. You can also use concateMap but that will not give you parallelism
                        .subscribeOn(Schedulers.parallel()))
                .flatMap(s->Flux.fromIterable(s));
// window operation will wait untill 2 element which will become flux of Flux<String> and then subscribeOn(which takes scheudler.parallel() which sends
        // Everything in a separate thread pool .  which goes from main thread to parallel thread.
        StepVerifier.create( stringFlux.log())
                .expectNextCount(12)
                .verifyComplete();
    }


}
