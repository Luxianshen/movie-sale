package com.github.lujs.community.api.model.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author joysim
 * @since 2020-03-27
 */
@Data
public class PostsQuery implements Serializable {

    /**
    * id 在查询业务一般是不需要的，这里是举个
    */
    private Integer type;

    /**
     * 是否获取新的数据 0否 1是
     */
    private Integer isRefresh;

    /**
     * 获取什么时间之前的
     */
    private Date begin;

    /**
     * 是否推荐 0否 1是
     */
    private Integer isRecommend;

    /**
     * 用户ID
     */
    @JsonSerialize(using= ToStringSerializer.class)
    private Long userId;

}
