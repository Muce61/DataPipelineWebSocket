package com.mc.common;

import lombok.Getter;

@Getter
public class HandshakeException extends RuntimeException {
    private final String jsonMessage;

    public HandshakeException(String jsonMessage) {
        super(jsonMessage);
        this.jsonMessage = jsonMessage;
    }

}
