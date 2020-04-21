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
@TableName("post_recommends")
public class PostRecommends extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("post_id")
    private Integer postId;

    @TableField("user_id")
    private Integer userId;

    @TableField("post_type")
    private Integer posType;

    /**
     * 推荐类型(0:永久在推荐表,1:暂时24小时在推荐表)
     */
    @TableField("recommend_type")
    private Integer recommendType;

}
