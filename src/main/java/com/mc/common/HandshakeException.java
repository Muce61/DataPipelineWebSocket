package com.mc.common;

import lombok.Getter;

@Getter
public class HandshakeException extends RuntimeException {
    private final String result;

    public HandshakeException(String result) {
        this.result = result;
    }

}
