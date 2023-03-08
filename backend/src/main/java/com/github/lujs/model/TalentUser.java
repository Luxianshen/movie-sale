package com.github.lujs.model;

import java.util.Date;

public class TalentUser {

    /*微信信息*/
    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 性别 0:未知  1：男  2：女
     */
    private Integer gender;


    /*基础信息*/
    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 首次访问时间
     */
    private Date firstTime;


 }
