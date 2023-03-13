package com.github.lujs.controller.util;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 22:13
 */
public class WechatUtil {

    /**
     * 生成微信签名
     * 返回相应信息到前端
     */
    public static Map<String, String> weChatPayInfo(String prepayId, String appId, String mchKey, String nonceStr, String outTradeNo) {

        Map<String, String> result = new HashMap<>(5);
        String packages = "prepay_id=" + prepayId;
        long timeStamp = DateUtil.currentSeconds();
        String sign = DigestUtils.md5Hex("appId=" + appId + "&nonceStr=" + nonceStr
                + "&package=" + packages + "&signType=MD5&timeStamp=" + timeStamp + "&key=" + mchKey).toUpperCase();
        result.put("timeStamp", String.valueOf(timeStamp));
        result.put("nonceStr", nonceStr);
        result.put("prepayId", "prepay_id=" + prepayId);
        result.put("sign", sign);
        result.put("outTradeNo", outTradeNo);
        return result;
    }

}
