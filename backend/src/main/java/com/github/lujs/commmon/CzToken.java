package com.github.lujs.commmon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
@Data
public class CzToken {

    /**
     * talentUserId
     */
    @JsonIgnore
    private Long id;

    /**
     * token令牌
     */
    private String token;

    /**
     * 用户名称
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 三方ID
     */
    private String bizUserId;

    private String lat;

    private String lon;

    private String cityName;

    private String cityCode;

    /**
     * sessionKey
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String sessionKey;

    @JsonIgnoreType
    public class IgnoreType {
    }

}
