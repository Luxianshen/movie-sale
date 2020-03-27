package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.PostViewsMapper;
import com.github.lujs.community.api.model.pojo.PostViews;
import com.github.lujs.community.api.service.IPostViewsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class PostViewsServiceImpl extends ServiceImpl<PostViewsMapper, PostViews> implements IPostViewsService {

}
