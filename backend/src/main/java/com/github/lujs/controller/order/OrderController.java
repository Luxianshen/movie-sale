package com.github.lujs.controller.order;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
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
import com.github.lujs.model.Result;
import com.github.lujs.model.enums.OrderState;
import com.github.lujs.model.pojo.Order;
import com.github.lujs.model.pojo.OrderDetail;
import com.github.lujs.service.IOrderDetailService;
import com.github.lujs.service.IOrderService;
import com.github.lujs.service.IOssService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
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

    @Resource
    private IOssService ossService;

    /**
     * 创建订单
     *
     * @return
     */
    @RequestMapping("/createOrder")
    public Result<String> createOrder(@Token CzToken token, @RequestBody OrderSaveRequest request) {

        BigDecimal price = new BigDecimal(request.getPrice());
        Integer buyNum = request.getBuyNum();
        Order order = new Order();
        order.init();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.init();
        orderDetail.setCinemaName(request.getCinemaName()).setCinemaAddress(request.getCinemaAddress())
                .setMovieName(request.getMovieName()).setMovieImg(request.getMovieImg())
                .setBuyNum(request.getBuyNum()).setPrice(price).setSeatInfo(request.getSeatInfo()).setHallName(request.getHallName())
                .setShowTime(request.getShowTime());

        BigDecimal total = NumberUtil.round(price.multiply(new BigDecimal(buyNum)), 2);
        order.setOrderState(OrderState.CREATE).setOrderType(0).setUserId(token.getId()).setUserMobile(token.getPhone())
                .setActualAmount(total).setCouponAmount(new BigDecimal(0)).setPayType(0)
                .setTotalAmount(total).setTransactionId(IdWorker.getIdStr());

        //todo 比对票价

        return Result.succeed(orderService.createOrder(order, orderDetail).toString());
    }

    @PostMapping("/payOrder/{orderId}")
    public Result<Map<String, String>> payOrder(@Token CzToken token, @PathVariable("orderId") Long orderId, HttpServletRequest request) {

        Order byId = orderService.getById(orderId);
        if (ObjectUtil.isNotNull(byId) && byId.getOrderState().equals(OrderState.CREATE)) {
            //比对演出剩余时间 要大于50分钟 不然 不让支付 浪费时间的订单

            WxPayService wxPayService = payConfiguration.wxPayService();
            //调起微信支付 必传下面参数
            WxPayUnifiedOrderRequest apiRequest = new WxPayUnifiedOrderRequest();
            apiRequest.setBody("订单支付");
            apiRequest.setOpenid(token.getBizUserId());
            apiRequest.setAppid(wxPayService.getConfig().getAppId());
            apiRequest.setMchId(wxPayService.getConfig().getMchId());
            apiRequest.setOutTradeNo(byId.getTransactionId());
            apiRequest.setTotalFee(NumberUtil.round(byId.getActualAmount().multiply(new BigDecimal(100)), 0).intValue());
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
                byId.setOrderState(OrderState.TO_PAY);
                byId.setUpdateTime(new Date());
                orderService.updateById(byId);
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
     * 关闭订单
     */
    @DeleteMapping("closeOrder/{orderId}")
    public Result<Void> closeOrder(@Token CzToken token, @PathVariable("orderId") String orderId) {

        Order byId = orderService.getById(orderId);
        if (ObjectUtil.isNotNull(byId) && token.getId().equals(byId.getUserId())) {
            byId.setUpdateTime(new Date());
            byId.setOrderState(OrderState.CANCEL);
            orderService.updateById(byId);
            return Result.succeed();
        }
        return Result.failed(null);
    }


    /**
     * 上传订单 取票二维码
     *
     * @return
     */
    @PostMapping("uploadTicketCode")
    public Result<Void> uploadTicketCode(/*@Token CzToken token,*/ @Valid @RequestBody OrderUploadRequest request) {

        OrderDetail byId = orderDetailService.getByOrderId(request.getOrderId());
        if (ObjectUtil.isNotNull(byId)) {
            byId.setUpdateTime(new Date());
            byId.setUploadTime(new Date());
            byId.setTicketCode(request.getTicketCode());
            byId.setTicketPic(ossService.uploadQrcode(request.getTicketCode()));
            orderService.updateOrderFinish(byId);
            return Result.succeed();
        }
        return Result.failed(null);
    }

}
