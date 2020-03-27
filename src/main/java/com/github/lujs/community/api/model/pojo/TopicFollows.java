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
@TableName("topic_follows")
public class TopicFollows extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("userId")
    private Integer userId;

    @TableField("topicId")
    private Integer topicId;

    /**
     * 是否关注话题
     */
    @TableField("hasFollow")
    private Boolean hasFollow;

    /**
     * 贡献积分
     */
    private Integer score;

    @TableField("followDate")
    private Date followDate;


}
