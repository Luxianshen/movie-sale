package com.github.lujs.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserMaLoginRequest {

    /**
     * 微信授权回执码
     */
    @NotEmpty(message = "授权码不能为空!")
    private String code;

    /**
     * encryptedData
     */
    private String encryptedData;

    /**
     * iv
     */
    private String iv;

}
