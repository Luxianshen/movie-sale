package com.github.lujs.commmon.query;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

/**
 * @author: binchao
 * @date: 2019-02-01 09:29
 * @desc:
 */
@Setter
@Getter
public class PageQuery<T, P> extends Page<T> {

    /**
     * 参数列表
     */
    @Valid
    private P params;
}
