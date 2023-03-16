package com.github.lujs.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.lujs.mapper.OrderDetailMapper;
import com.github.lujs.mapper.OrderMapper;
import com.github.lujs.model.enums.OrderState;
import com.github.lujs.model.page.CzOrderPage;
import com.github.lujs.model.pojo.Order;
import com.github.lujs.model.pojo.OrderDetail;
import com.github.lujs.service.IOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:52
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Resource
    private OrderDetailMapper orderDetailMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Long createOrder(Order order, OrderDetail orderDetail) {
        baseMapper.insert(order);
        Long orderId = order.getId();
        orderDetail.setId(orderId);
        orderDetailMapper.insert(orderDetail);
        return orderId;
    }

    @Override
    public void handlePayResult(String outTradeNo, String transactionId) {
        Order order = baseMapper.selectOne(new LambdaQueryWrapper<>(Order.class).eq(Order::getTransactionId, transactionId).last("limit 1"));
        if (ObjectUtil.isNotEmpty(order)) {
            if (order.getOrderState().equals(OrderState.TO_PAY)) {
                //有订单 状态为待支付 更新为已支付 其他不处理
                order.setPayTime(new Date());
                order.setOrderState(OrderState.PIED);
                order.setUpdateTime(new Date());
                baseMapper.updateById(order);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateOrderFinish(OrderDetail byId) {
        Order order = baseMapper.selectById(byId.getOrderId());
        order.setOrderState(OrderState.FINISH);
        order.setUpdateTime(new Date());
        baseMapper.updateById(order);
        orderDetailMapper.updateById(byId);
    }

    @Override
    public Page<CzOrderPage> myOrderPage(Long userId, Integer offset) {

        Page<CzOrderPage> page = new Page<>();
        page.setPages(offset);
        baseMapper.myOrderPage(page, userId);

        return page;
    }


}
