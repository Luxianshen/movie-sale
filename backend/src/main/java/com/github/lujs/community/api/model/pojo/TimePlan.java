package com.github.lujs.community.api.model.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.github.lujs.commmon.BaseEntity;
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
@TableName("time_plan")
public class TimePlan extends BaseEntity {

private static final long serialVersionUID=1L;

    /**
     * 计划名称
     */
    private String name;

    /**
     * 发起人
     */
    private String promoter;

    /**
     * 参与人
     */
    private String participants;

    /**
     * 经度
     */
    private Double latitude;
    
    /**
     * 维度
     */
    private Double longitude;

    /**
     * 计划实现时间
     */
    private Date planTime;

}
