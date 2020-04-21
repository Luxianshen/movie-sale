package com.github.lujs.community.api.model.pojo;

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

    @TableField("icon_src")
    private String iconSrc;

    /**
     * 话题用户别称
     */
    @TableField("nick_name")
    private String nickName;

    /**
     * 话题类型:(0:系统话题,1:用户自定义话题)
     */
    @TableField("topic_type")
    private Integer topicType;

    /**
     * 话题创造者
     */
    @TableField("owner_id")
    private Long ownerId;

    @TableField("follow_count")
    private Integer followCount;

    @TableField("post_count")
    private Integer postCount;

    @TableField("is_top")
    private Boolean isTop;

    @TableField("is_activity")
    private Boolean isActivity;


}
