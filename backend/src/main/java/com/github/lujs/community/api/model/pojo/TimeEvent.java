package com.github.lujs.community.api.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.github.lujs.commmon.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @author joysim
 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("time_event")
public class TimeEvent extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 标题
     */
    private String title;

    /**
     * 图标
     */
    private String icon;
    
    /**
     * 颜色
     */
    private String color;

    /**
     * 月和日
     */
    private String monthDay;

    /**
     * 时分
     */
    private String hourMin;
}
