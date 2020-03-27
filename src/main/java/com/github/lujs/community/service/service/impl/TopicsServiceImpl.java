package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.TopicsMapper;
import com.github.lujs.community.api.model.pojo.Topics;
import com.github.lujs.community.api.service.ITopicsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class TopicsServiceImpl extends ServiceImpl<TopicsMapper, Topics> implements ITopicsService {

}
