package com.github.lujs.community.api.model.pojo;

import com.github.lujs.commmon.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 
 * @author joysim
 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("trades")
public class Trades extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("user_id")
    private Integer userId;

    @TableField("from_id")
    private Integer fromId;

    private Integer shell;

    private Integer money;

    /**
     * 交易类型(0:变现,1:转换)
     */
    private Integer type;

    /**
     * 变现状态(0:申请中,1:已发放,2:已拒绝)
     */
    private Integer state;

    /**
     * 申请时间
     */
    @TableField("addDate")
    private Date addDate;

    /**
     * 发放时间
     */
    @TableField("senDate")
    private Date senDate;


}
