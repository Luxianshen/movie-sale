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

    @TableField("userId")
    private Integer userId;

    @TableField("toId")
    private Integer toId;

    @TableField("postId")
    private Integer postId;

    @TableField("commentId")
    private Integer commentId;

    /**
     * 类型:0帖子,1评论
     */
    @TableField("likeType")
    private Integer likeType;

    @TableField("senDate")
    private Date senDate;

    @TableField("isRead")
    private Boolean isRead;


}
