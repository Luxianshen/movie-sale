package com.github.lujs.community.service.mapper;

import com.github.lujs.community.api.model.pojo.PostLikes;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  Mapper 接口
 * @author joysim
 * @since 2020-03-27
 */
public interface PostLikesMapper extends BaseMapper<PostLikes> {

    List<PostLikes> getByPostId(@Param("id") Long id);
}
