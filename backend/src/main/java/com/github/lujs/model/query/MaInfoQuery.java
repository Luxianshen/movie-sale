package com.github.lujs.model.query;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class MaInfoQuery {
    /**
     * 小程序APPID
     */
    @NotEmpty(message = "appId不为空")
    private String appId;

    @NotEmpty(message = "sessionKey不为空")
    private String sessionKey;

    @NotEmpty(message = "encryptedData不为空")
    private String encryptedData;

    @NotEmpty(message = "iv不为空")
    private String iv;

    /**
     * 绑定手机号时必填
     */
    private String bizUserId;

}
