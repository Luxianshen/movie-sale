package com.github.lujs.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.lujs.model.Order;
import com.github.lujs.model.OrderDetail;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:39
 */
public interface IOrderDetailService extends IService<OrderDetail> {

    OrderDetail getByOrderId(Long orderId);
}
