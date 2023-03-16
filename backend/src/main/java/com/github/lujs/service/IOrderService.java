package com.github.lujs.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.lujs.model.page.CzOrderPage;
import com.github.lujs.model.pojo.Order;
import com.github.lujs.model.pojo.OrderDetail;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:39
 */
public interface IOrderService extends IService<Order> {

    Long createOrder(Order order, OrderDetail orderDetail);

    void handlePayResult(String outTradeNo, String transactionId);

    void updateOrderFinish(OrderDetail byId);

    /**
     * 根据用户id  查询订单列表
     *
     * @param id
     * @param offset
     */
    Page<CzOrderPage> myOrderPage(Long id, Integer offset);

}
