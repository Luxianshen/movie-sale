package com.github.lujs.commmon.controller.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author: binchao
 * @date: 2019-02-14 09:19
 * @desc: 通过主键查询请求
 */
@Data
public class PrimaryKeyRequest implements Serializable {

    /**
     * id
     */
    @NotNull(message = "id必填")
    private Long id;
}
