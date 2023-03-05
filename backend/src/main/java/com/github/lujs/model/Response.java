package com.github.lujs.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/4 17:55
 */
@AllArgsConstructor
@Data
public class Response {

    private Integer code;
    private String message;
    private Object data;

}
