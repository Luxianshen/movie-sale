package com.github.lujs.controller.order;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lujs.commmon.CzToken;
import com.github.lujs.commmon.annotation.Token;
import com.github.lujs.model.Result;
import com.github.lujs.model.page.CzOrderPage;
import com.github.lujs.model.pojo.OrderDetail;
import com.github.lujs.service.IOrderDetailService;
import com.github.lujs.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:01
 */
@Slf4j
@RestController
@RequestMapping("/cz/order")
public class CzOrderController {


    @Resource
    private IOrderService orderService;

    @Resource
    private IOrderDetailService orderDetailService;


    /**
     * 订单列表
     *
     * @return
     */
    @RequestMapping("/myOrderPage/{offset}")
    public Result<Page<CzOrderPage>> myOrderPage(@Token CzToken token, @PathVariable("offset") Integer offset) {

        return Result.succeed(orderService.myOrderPage(token.getId(), offset));
    }


    /**
     * 订单列表
     *
     * @return
     */
    @RequestMapping("/myOrderDetail/{orderId}")
    public Result<OrderDetail> myOrderDetail(@Token CzToken token, @PathVariable("orderId") Long orderId) {

        if (ObjectUtil.isNotNull(token.getId())) {
            OrderDetail orderDetail = orderDetailService.getByOrderId(orderId);
            return Result.succeed(orderDetail);
        }
        log.info("用户未授权手机号！不可能存在订单");
        return Result.failed(null);
    }


}
