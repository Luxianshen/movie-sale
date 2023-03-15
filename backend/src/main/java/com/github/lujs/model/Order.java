package com.github.lujs.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
@TableName("tb_order")
public class Order extends BaseEntity {


    private static final long serialVersionUID = 1L;

    /**
     * 第三方user表id
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;


    /**
     * 用户手机号码
     */
    private String userMobile;

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单状态 (-15=已退款 -10=退款进行中 -5=已取消 0=待支付 5=待使用 10=已核销 15=已评价)
     */
    private Integer orderState;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * '订单实付'
     */
    private BigDecimal actualAmount;

    /**
     * 订单类型 0=普通订单
     */
    private Integer orderType;

    /**
     * 优惠券优惠金额
     */
    private BigDecimal couponAmount;

    /**
     * 支付类型 0 微信支付
     */
    private Integer payType;

    /**
     * 第三方支付流水号 (例如wxPay的transaction_id)
     */
    private String transactionId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;


}
