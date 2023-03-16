package com.github.lujs.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.lujs.mapper.OrderDetailMapper;
import com.github.lujs.model.pojo.OrderDetail;
import com.github.lujs.service.IOrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:52
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {


    @Override
    public OrderDetail getByOrderId(Long orderId) {
        return lambdaQuery().eq(OrderDetail::getOrderId,orderId).last("limit 1").one();
    }

}
