package com.github.lujs.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private static final Integer SUCCESS = 0;

    private T data;
    private Integer code;
    private String msg;

    public static <T> Result<T> succeed(T model) {
        return of(model, SUCCESS, "");
    }

    public static <T> Result<T> succeed() {
        return new Result<>(null, SUCCESS, "");
    }

    public static <T> Result<T> of(T data, Integer code, String msg) {
        return new Result<>(data, code, msg);
    }

    public static <T> Result<T> failed(T model) {
        return of(model, 1,"失败了");
    }


}
