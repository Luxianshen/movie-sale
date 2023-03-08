package com.github.lujs.model;

import lombok.Data;

@Data
public class UserDto {
    /**
     * id
     */
    private Long id;

    /**
     * 用户名
     */
    private String agentId;

    /**
     * 用户状态
     */
    private Integer state;

    /**
     * 用户姓名
     */
    private String name;

    /**
     * 用户头像地址
     */
    private String avatar;

    /**
     * 用户性别
     */
    private Integer gender;

    /**
     * 用户电话号码
     */
    private String phone;

    /**
     * 三方用户ID
     */
    private String bizUserId;

    /**
     * sessionKey
     */
    private String sessionKey;

}
