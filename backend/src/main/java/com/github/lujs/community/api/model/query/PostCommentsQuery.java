package com.github.lujs.community.api.model.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author joysim
 * @since 2020-03-27
 */
@Data
public class PostCommentsQuery implements Serializable {

    /**
    * id 在查询业务一般是不需要的，这里是举个例子
    */
    @JsonSerialize( using = ToStringSerializer.class)
    private Long postId;

}
