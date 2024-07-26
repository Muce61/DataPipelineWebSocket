package com.mc.enums;

import lombok.Getter;

@Getter
public enum Members {
    ADMIN(1, "admin");

    private final Integer code;
    private final String msg;

    Members(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
