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
@TableName("user_messages")
public class UserMessages extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("toUserId")
    private Integer toUserId;

    @TableField("messageId")
    private Integer messageId;

    @TableField("senDate")
    private Date senDate;

    @TableField("isRead")
    private Boolean isRead;


}
