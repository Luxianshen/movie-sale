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
@TableName("user_follows")
public class UserFollows extends BaseEntity {

private static final long serialVersionUID=1L;

    @TableField("fromId")
    private Integer fromId;

    @TableField("toId")
    private Integer toId;

    @TableField("followDate")
    private Date followDate;


}