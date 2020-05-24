package com.github.lujs.community.service.mapper;

import com.github.lujs.community.api.model.pojo.PostComments;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 *  Mapper 接口
 * @author joysim
 * @since 2020-03-27
 */
public interface PostCommentsMapper extends BaseMapper<PostComments> {

    List<PostComments> getByPostId(Long id);
}
