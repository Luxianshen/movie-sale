package com.github.lujs.community.api.model.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.github.lujs.commmon.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author joysim
 * @since 2020-03-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("posts")
public class Posts extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("user_id")
    private Integer userId;

    @TableField("question_id")
    private Integer questionId;

    @TableField("article_title")
    private String articleTitle;

    @TableField("article_img")
    private String articleImg;

    @TableField("article_html")
    private String articleHtml;

    @TableField("article_delta")
    private String articleDelta;

    private String introduction;

    private String content;

    private String imgs;

    /**
     * 帖子类型(0:动弹,1:文章,2:问答,3:投票)
     */
    @TableField("pos_type")
    private Integer posType;

    private String link;

    private String video;

    private String audio;

    @TableField("thumbs_count")
    private Integer thumbsCount;

    @TableField("comment_count")
    private Integer commentCount;

    @TableField("view_count")
    private Integer viewCount;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long topicId;

    @TableField("topic_title")
    private String topicTitle;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    @TableField("is_recommend")
    private Boolean isRecommend;

    @TableField("is_top")
    private Boolean isTop;

    /**
     * 累计打赏玉帛贝数量
     */
    private Integer shell;


}
