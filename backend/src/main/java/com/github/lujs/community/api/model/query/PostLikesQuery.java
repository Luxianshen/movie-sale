package com.github.lujs.community.api.model.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @author joysim
 * @since 2020-03-27
 */
@Data
public class PostLikesQuery implements Serializable {
    private Long id;
    /**
    * id 在查询业务一般是不需要的，这里是举个例子
        private String number;
    */
}
