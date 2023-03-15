package com.github.lujs.controller.request;

import lombok.Data;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 21:31
 */
@Data
public class OrderSaveRequest {

    /**
     * 影院名称
     */
    private String cinemaName;

    /**
     * 影院地址
     */
    private String cinemaAddress;

    /**
     * 电影开始时间
     */
    private String showTime;

    /**
     * 厅的名称
     */
    private String hallName;

    /**
     * 座位数
     */
    private Integer buyNum;

    /**
     * 单价
     */
    private String price;

    /**
     * 座号
     */
    private String seatInfo;

}
