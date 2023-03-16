package com.github.lujs.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.github.lujs.commmon.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("tb_order_detail")
public class OrderDetail extends BaseEntity {


    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    private Long orderId;


    /**
     * 影院名称
     */
    private String cinemaName;

    /**
     * 影院地址
     */
    private String cinemaAddress;

    /**
     * 放映厅名称
     */
    private String hallName;

    /**
     * 开始时间
     */
    private String showTime;

    /**
     * 购买票数
     */
    private Integer buyNum;

    /**
     * 单票价格
     */
    private BigDecimal price;

    /**
     * 成本
     */
    private BigDecimal cost;

    /**
     * 座位信息
     */
    private String seatInfo;

    /**
     * 是否接受调座
     */
    private boolean seatAdjustment;


    /**
     * 电影票 二维码 多个 ；隔开
     */
    private String ticketCode;

    /**
     * 电影票 兑换码 多个 ；隔开
     */
    private String ticketNum;

    /**
     * 电影票 凭证
     */
    private String ticketPic;

    /**
     * 上传时间
     */
    private Date uploadTime;

}
