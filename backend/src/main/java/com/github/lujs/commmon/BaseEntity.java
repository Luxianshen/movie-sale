package com.github.lujs.commmon;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.lujs.commmon.enums.DataLogicState;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 数据表通用模型
 * @author binchao
 */
@Data
public class BaseEntity implements Serializable {

    /**
     * 主键
     */
    @TableId
    @JsonSerialize(using= ToStringSerializer.class)
    private Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除（逻辑）
     */
    private DataLogicState deleted;

    public void init() {
        this.createTime = new Date();
        this.deleted = DataLogicState.NOT_DELETED;
    }
}
