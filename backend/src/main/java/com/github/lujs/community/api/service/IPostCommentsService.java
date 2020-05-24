package com.github.lujs.community.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.lujs.community.api.model.pojo.PostComments;

import java.util.List;

/**
 *  服务类
 * @author joysim
 * @since 2020-03-27
 */
public interface IPostCommentsService extends IService<PostComments> {

    List<PostComments> getByPostId(Long id);
}
