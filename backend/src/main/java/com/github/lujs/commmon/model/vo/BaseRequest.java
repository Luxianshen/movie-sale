package com.github.lujs.commmon.model.vo;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 标准请求报文
 * @param <T>
 * @author binchao
 */
@Data
public class BaseRequest<T> implements Serializable {

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     *  签名字符串
     */
    private String sign;

    /**
     * 业务数据
     */
    @Valid
    @NotNull
    private T data;

}
