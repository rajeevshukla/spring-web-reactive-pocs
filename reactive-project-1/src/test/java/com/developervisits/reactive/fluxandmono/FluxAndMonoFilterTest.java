package com.developervisits.reactive.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {
    List<String> list = Arrays.asList("Spring", "Core","Test", "Work");
    @Test
    public void filterTest() {
        Flux<String> stringFlux = Flux.fromIterable(list);
        stringFlux = stringFlux.filter(name->name.startsWith("T")).log();

        StepVerifier.create(stringFlux)
                .expectNext("Test")
                .verifyComplete();
    }

    @Test
    public void filterTest_WithCount() {
        Flux<String> stringFlux = Flux.fromIterable(list);
        stringFlux = stringFlux.filter(name->name.length()>4).log();

        StepVerifier.create(stringFlux)
                .expectNext("Spring")
                .verifyComplete();
    }
}
