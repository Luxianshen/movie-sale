package com.github.lujs.community.api.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("post_comments")
public class PostComments extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("form_id")
    private Integer fromId;

    @TableField("to_id")
    private Integer toId;

    @TableField("post_id")
    private Integer postId;

    @TableField("comment_id")
    private Integer commentId;

    @TableField("reply_id")
    private Integer replyId;

    /**
     * 0:评论帖子,1:回复评论,2:回复评论的回复
     */
    @TableField("comment_type")
    private Integer commentType;

    private String content;

    private String imgs;

    @TableField("thumbs_count")
    private Integer thumbsCount;

    @TableField("reply_count")
    private Integer replyCount;

    @TableField("is_read")
    private Boolean isRead;

    @TableField("is_hot")
    private Boolean isHot;


}
