package com.github.lujs.community.api.model.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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

    @JsonSerialize(using= ToStringSerializer.class)
    private Long fromId;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long toId;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long postId;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long commentId;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long replyId;

    /**
     * 0:评论帖子,1:回复评论,2:回复评论的回复
     */
    private Integer commentType;

    private String content;

    private String imgs;

    private Integer thumbsCount;

    private Integer replyCount;

    private Boolean isRead;

    private Boolean isHot;


}
