package com.github.lujs.commmon.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 键值枚举
 * @author binchao
 */
public interface ValueEnum {

    /**
     * 状态码
     * @return
     */
    @JsonValue
    Integer getCode();

    /**
     * 状态值
     * @return
     */
    String getText();
}
