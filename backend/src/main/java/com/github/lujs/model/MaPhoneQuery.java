package com.github.lujs.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MaPhoneQuery {

    /**
     * 小程序APPID
     */
    @NotEmpty(message = "appId不为空")
    private String appId;

    @NotEmpty(message = "encryptedData不为空")
    private String encryptedData;

    @NotEmpty(message = "iv不为空")
    private String iv;

    //@NotNull(message = "用户类型不为空")
    private Integer userType;

    /**
     * 绑定手机号时必填
     */
    private String bizUserId;

}
