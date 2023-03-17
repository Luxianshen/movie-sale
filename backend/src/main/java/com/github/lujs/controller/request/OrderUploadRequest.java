package com.github.lujs.controller.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 21:31
 */
@Data
public class OrderUploadRequest {

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 兑换code 生成二维码
     */
    @NotEmpty
    private String ticketCode;

}
