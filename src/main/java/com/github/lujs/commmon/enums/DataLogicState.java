package com.github.lujs.commmon.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * 数据逻辑状态
 * @author binchao
 */
public enum DataLogicState implements ValueEnum {

    NOT_DELETED(0, "未删除"),
    DELETED(1, "已删除");

    @EnumValue
    private Integer code;

    private String text;

    DataLogicState(Integer code, String text) {
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
