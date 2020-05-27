package com.github.lujs.community.api.model.pojo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.lujs.commmon.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("topics")
public class Topics extends BaseEntity {

private static final long serialVersionUID=1L;

    private String title;

    private String des;

    private String iconSrc;

    /**
     * 话题用户别称
     */
    private String nickName;

    /**
     * 话题类型:(0:系统话题,1:用户自定义话题)
     */
    private Integer topicType;

    /**
     * 话题创造者
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long ownerId;

    private Integer followCount;

    private Integer postCount;

    private Boolean isTop;

    private Boolean isActivity;

}
