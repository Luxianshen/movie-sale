package com.github.lujs.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.github.lujs.commmon.enums.ValueEnum;

public enum OrderState implements ValueEnum {

    CANCEL(-1,"订单取消"),
    CREATE(0, "未支付"),
    TO_PAY(1, "待支付"),
    PIED(2, "已支付"),
    FINISH(3, "已完成");


    @EnumValue
    private final Integer code;

    private final String text;

    OrderState(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getText() {
        return text;
    }
}

