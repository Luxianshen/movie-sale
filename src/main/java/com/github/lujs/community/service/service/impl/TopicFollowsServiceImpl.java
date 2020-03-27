package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.TopicFollowsMapper;
import com.github.lujs.community.api.model.pojo.TopicFollows;
import com.github.lujs.community.api.service.ITopicFollowsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class TopicFollowsServiceImpl extends ServiceImpl<TopicFollowsMapper, TopicFollows> implements ITopicFollowsService {

}
