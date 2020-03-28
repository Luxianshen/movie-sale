package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.PostsMapper;
import com.github.lujs.community.api.model.pojo.Posts;
import com.github.lujs.community.api.service.IPostsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts> implements IPostsService {

}
