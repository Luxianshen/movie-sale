package com.github.lujs.commmon.model.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.lujs.commmon.enums.ValueEnum;
import com.github.lujs.commmon.exception.status.GlobalStatusCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 标准响应报文
 * @author mobinchao
 */
@JsonPropertyOrder({"code", "msg", "data"})
@Data
public class BaseResponse<T> implements Serializable {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 状态码描述
     */
    private String msg;

    /**
     * 业务数据
     */
    private T data;

    public BaseResponse() {
        this.setSuccess();
    }

    public BaseResponse(String msg) {
        this.msg = msg;
    }

    public BaseResponse(ValueEnum statusCode) {
        this.setStatus(statusCode);
    }

    public BaseResponse(T data) {
        this.data = data;
    }

    public void setFailed() {
        this.setStatus(GlobalStatusCode.FAILED);
    }
    public void setSuccess() {
        this.setStatus(GlobalStatusCode.SUCCESS);
    }

    public void setStatus(ValueEnum status) {
        this.code = status.getCode();
        this.msg = status.getText();
    }
}
