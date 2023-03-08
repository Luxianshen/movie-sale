package com.github.lujs.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.lujs.commmon.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@TableName("tb_talent_third")
public class TalentThird extends BaseEntity {

    /**
     * 三方应用id
     */
    private String appId;

    /**
     * 三方openid
     */
    private String openId;

    /**
     * 三方unionid
     */
    private String unionId;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 绑定的达人用户id
     */
    private Long userId;

    /**
     * 历史手机号
     */
    private String usesPhone;

}
