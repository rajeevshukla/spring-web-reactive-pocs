package com.developervisits.reactive.fluxandmono;

public class CustomExeption extends Throwable {
    public CustomExeption(String message) {
    }

    public CustomExeption(Throwable message) {
        super(message);
    }


}
