package com.github.lujs.model.page;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class CzOrderPage {

    private Long id;

    private Integer orderState;

    private BigDecimal actualAmount;

    private Date createTime;

    private String cinemaName;

    private String hallName;

    private String showTime;

    private String seatInfo;

}
