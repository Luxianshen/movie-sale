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
@TableName("messages")
public class Messages extends BaseEntity {

private static final long serialVersionUID=1L;

    private String content;

    /**
     * 0:文本,1:富文本
     */
    @TableField("content_type")
    private Integer contentType;

    @TableField("create_date")
    private Date createDate;


}
