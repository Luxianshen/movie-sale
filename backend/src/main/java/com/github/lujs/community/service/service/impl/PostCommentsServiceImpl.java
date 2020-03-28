package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.PostCommentsMapper;
import com.github.lujs.community.api.model.pojo.PostComments;
import com.github.lujs.community.api.service.IPostCommentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class PostCommentsServiceImpl extends ServiceImpl<PostCommentsMapper, PostComments> implements IPostCommentsService {

}
