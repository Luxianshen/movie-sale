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
@TableName("post_likes")
public class PostLikes extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("user_id")
    private Integer userId;

    @TableField("to_id")
    private Integer toId;

    @TableField("post_id")
    private Integer postId;

    @TableField("comment_id")
    private Integer commentId;

    /**
     * 类型:0帖子,1评论
     */
    @TableField("like_type")
    private Integer likeType;

    @TableField("is_read")
    private Boolean isRead;


}
