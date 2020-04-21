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
@TableName("user_sys_messages")
public class UserSysMessages extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("to_id")
    private Integer toId;

    @TableField("message_id")
    private Integer messageId;

    @TableField("is_read")
    private Boolean isRead;


}
