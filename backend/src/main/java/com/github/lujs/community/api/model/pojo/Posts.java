package com.github.lujs.community.api.model.pojo;

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

    @TableField("userId")
    private Integer userId;

    @TableField("questionId")
    private Integer questionId;

    @TableField("articleTitle")
    private String articleTitle;

    @TableField("articleImg")
    private String articleImg;

    @TableField("articleHtml")
    private String articleHtml;

    @TableField("articleDelta")
    private String articleDelta;

    private String introduction;

    private String content;

    private String imgs;

    /**
     * 帖子类型(0:动弹,1:文章,2:问答,3:投票)
     */
    @TableField("posType")
    private Integer posType;

    private String link;

    private String video;

    private String audio;

    /**
     * 发布时间
     */
    @TableField("senDate")
    private Date senDate;

    @TableField("thumbsCount")
    private Integer thumbsCount;

    @TableField("commentCount")
    private Integer commentCount;

    @TableField("viewCount")
    private Integer viewCount;

    @TableField("topicId")
    private Integer topicId;

    @TableField("topicTitle")
    private String topicTitle;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    @TableField("isRecommend")
    private Boolean isRecommend;

    @TableField("isTop")
    private Boolean isTop;

    /**
     * 累计打赏玉帛贝数量
     */
    private Integer shell;


}
