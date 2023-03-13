package com.github.lujs.controller.order;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.lujs.commmon.CzToken;
import com.github.lujs.commmon.annotation.Token;
import com.github.lujs.commmon.config.WxPayConfiguration;
import com.github.lujs.controller.request.OrderSaveRequest;
import com.github.lujs.controller.request.OrderUploadRequest;
import com.github.lujs.controller.util.WechatUtil;
import com.github.lujs.model.Order;
import com.github.lujs.model.OrderDetail;
import com.github.lujs.model.Result;
import com.github.lujs.service.IOrderDetailService;
import com.github.lujs.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:01
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {


    @Resource
    private WxPayConfiguration payConfiguration;

    @Resource
    private IOrderService orderService;

    @Resource
    private IOrderDetailService orderDetailService;

    @Value("${domain}")
    private String domain;

    /**
     * 创建订单
     *
     * @return
     */
    @PostMapping("/createOrder")
    public Result<Void> createOrder(@Token CzToken token, @RequestBody OrderSaveRequest request) {

        BigDecimal price = new BigDecimal(request.getPrice());
        Integer buyNum = request.getBuySeat();
        Order order = new Order();
        order.init();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.init();
        orderDetail.setOrderId(order.getId()).setCinemaName(request.getCinemaName()).setCinemaAddress(request.getCinemaAddress())
                .setBuyNum(request.getBuySeat()).setPrice(price).setSeatInfo(request.getSeatInfo());

        BigDecimal total = NumberUtil.round(price.multiply(new BigDecimal(buyNum)), 2);
        order.setOrderState(0).setOrderType(0).setUserId(token.getId()).setUserMobile(token.getPhone())
                .setActualAmount(total).setCouponAmount(new BigDecimal(0)).setPayType(0)
                .setTotalAmount(total);

        orderService.createOrder(order, orderDetail);
        return Result.succeed();
    }

    @PostMapping("/payOrder/{orderId}")
    public Result<Map<String, String>> payOrder(@Token CzToken token, @PathVariable("orderId") Long orderId, HttpServletRequest request) {

        Order byId = orderService.getById(orderId);
        if (ObjectUtil.isNotNull(byId) && byId.getOrderState().equals(0)) {
            WxPayService wxPayService = payConfiguration.wxPayService();
            //调起微信支付 必传下面参数
            WxPayUnifiedOrderRequest apiRequest = new WxPayUnifiedOrderRequest();
            apiRequest.setBody("订单支付");
            apiRequest.setOpenid(token.getBizUserId());
            apiRequest.setMchId(wxPayService.getConfig().getMchId());
            apiRequest.setOutTradeNo(byId.getTransactionId());
            apiRequest.setTotalFee(NumberUtil.round(byId.getActualAmount().multiply(new BigDecimal(100)),0).intValue());
            apiRequest.setSpbillCreateIp(request.getRemoteAddr());
            apiRequest.setTradeType("JSAPI");
            apiRequest.setNonceStr(IdWorker.getIdStr());
            apiRequest.setNotifyUrl(domain + "/order/orderCall/");

            log.debug("【TbPeccOrderServiceImpl - buildOrder】mchId: ");
            try {
                WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(apiRequest);
                //保存微信返回信息
                log.debug("wxPayService.unifiedOrder: {}", JSONUtil.toJsonStr(result));
                if (result == null) {
                    throw new RuntimeException("微信统一下单异常");
                }
                //返回成功
                Map<String, String> resultMap = WechatUtil.weChatPayInfo(result.getPrepayId(), wxPayService.getConfig().getAppId(), wxPayService.getConfig().getMchKey(),
                        apiRequest.getNonceStr(), apiRequest.getOutTradeNo());
                return Result.succeed(resultMap);
            } catch (WxPayException e) {
                log.error("【TbPeccOrderServiceImpl - buildOrder】error: {}", e.getMessage(), e);
                throw new RuntimeException("微信统一下单失败：{}" + e.getMessage());
            }
        }
        return Result.failed(null);
    }


    @RequestMapping("/orderCall")
    public String parseOrderNotifyResult(@RequestBody String xmlData) {
        log.info("微信支付回调数据:{}", xmlData);
        try {
            WxPayService wxPayService = payConfiguration.wxPayService();
            final WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
            String outTradeNo = notifyResult.getOutTradeNo();

            log.info("【notifyResult】:{}", notifyResult);

            boolean result = "SUCCESS".equals(notifyResult.getResultCode());
            String transactionId = notifyResult.getTransactionId();
            if (result)
                orderService.handlePayResult(outTradeNo, transactionId);

        } catch (Exception e) {
            log.error("微信回调结果异常,异常原因", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
        return WxPayNotifyResponse.success("成功");
    }

    /**
     * 上传订单 取票二维码
     *
     * @return
     */
    @PostMapping("uploadTicketCode")
    public Result<Void> uploadTicketCode(@Token CzToken token, OrderUploadRequest request) {

        OrderDetail byId = orderDetailService.getByOrderId(request.getOrderId());
        if (ObjectUtil.isNotNull(byId)) {
            if (ObjectUtil.isNotEmpty(request.getTicketCode()))
                byId.setTicketCode(request.getTicketCode());
            if (ObjectUtil.isNotEmpty(request.getTicketNum()))
                byId.setTicketNum(request.getTicketNum());
            if (ObjectUtil.isNotEmpty(request.getTicketPic()))
                byId.setTicketPic(request.getTicketPic());
            byId.setUpdateTime(new Date());
            byId.setUploadTime(new Date());
            orderService.updateOrderFinish(byId);
            return Result.succeed();
        }
        return Result.failed(null);
    }

}
