package com.github.lujs.community.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.lujs.community.api.model.pojo.PostLikes;
import com.github.lujs.community.api.service.IPostLikesService;
import com.github.lujs.community.service.mapper.PostLikesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class PostLikesServiceImpl extends ServiceImpl<PostLikesMapper, PostLikes> implements IPostLikesService {

    @Override
    public List<PostLikes> getByPostId(Long id) {
        return baseMapper.getByPostId(id);
    }
}
