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

    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

    private Integer questionId;

    private String articleTitle;

    private String articleImg;

    private String articleHtml;

    private String articleDelta;

    private String introduction;

    private String content;

    private String imgs;

    /**
     * 帖子类型(0:动弹,1:文章,2:问答,3:投票)
     */
    private Integer posType;

    private String link;

    private String video;

    private String audio;

    private Integer thumbsCount;

    private Integer commentCount;

    private Integer viewCount;

    @JsonSerialize(using= ToStringSerializer.class)
    private Long topicId;

    private String topicTitle;

    private BigDecimal latitude;

    private BigDecimal longitude;

    private String address;

    private Boolean isRecommend;

    private Boolean isTop;

    /**
     * 累计打赏玉帛贝数量
     */
    private Integer shell;


}
