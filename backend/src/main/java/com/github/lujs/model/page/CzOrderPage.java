package com.github.lujs.model.page;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CzOrderPage {

    private Long id;

    private Integer orderState;

    /**
     * 电影名称
     */
    private String movieName;

    /**
     * 电影照片
     */
    private String movieImg;

    private Integer buyNum;

    private BigDecimal actualAmount;

    private Date createTime;

    private String cinemaName;

    private String hallName;

    private String showTime;

    private String seatInfo;

    private String ticketPic;

}
