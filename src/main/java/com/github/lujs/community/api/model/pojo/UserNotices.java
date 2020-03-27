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
@TableName("user_notices")
public class UserNotices extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("toId")
    private Integer toId;

    @TableField("fromId")
    private Integer fromId;

    @TableField("postId")
    private Integer postId;

    @TableField("commentId")
    private Integer commentId;

    @TableField("replyId")
    private Integer replyId;

    /**
     * 0:点赞帖子，1:点赞评论，2:评论，3:回复，4:关注，5:回答
     */
    @TableField("noticeType")
    private Integer noticeType;

    @TableField("senDate")
    private Date senDate;

    @TableField("isRead")
    private Boolean isRead;

    private Integer shell;


}
