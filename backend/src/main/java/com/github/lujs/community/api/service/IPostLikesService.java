package com.github.lujs.community.api.service;

import com.github.lujs.community.api.model.pojo.PostLikes;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  服务类
 * @author joysim
 * @since 2020-03-27
 */
public interface IPostLikesService extends IService<PostLikes> {

    List<PostLikes> getByPostId(Long id);
}
