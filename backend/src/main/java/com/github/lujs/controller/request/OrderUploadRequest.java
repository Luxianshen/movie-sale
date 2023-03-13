package com.github.lujs.controller.request;

import lombok.Data;

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
    private String ticketCode;

    /**
     * 兑换码
     */
    private String ticketNum;

    /**
     * 二维码照片
     */
    private String ticketPic;

}
