package com.mc.common;

import com.mc.enums.RespEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> implements Serializable {
    private static final long serialVersionUID = -5270573611742584766L;
    private T data;
    private Integer code;
    private String message;

    public static <T> Result<T> success(T data, String message) {
        Result<T> resultData = new Result<>();
        resultData.setCode(200);
        resultData.setMessage(message);
        resultData.setData(data);
        return resultData;
    }

    public static <T> Result<T> of(int code, String message) {
        Result<T> resultData = new Result<>();
        resultData.setCode(code);
        resultData.setMessage(message);
        return resultData;
    }

    public static <T> Result<T> of(RespEnum enums) {
        Result<T> resultData = new Result<>();
        resultData.setCode(enums.getCode());
        resultData.setMessage(enums.getMsg());
        return resultData;
    }

    public static <T> Result<T> of(T data, RespEnum enums) {
        Result<T> resultData = new Result<>();
        resultData.setData(data);
        resultData.setCode(enums.getCode());
        resultData.setMessage(enums.getMsg());
        return resultData;
    }

    public static <T> Result<T> of(T data, int code, String msg) {
        Result<T> resultData = new Result<>();
        resultData.setData(data);
        resultData.setCode(code);
        resultData.setMessage(msg);
        return resultData;
    }
}
