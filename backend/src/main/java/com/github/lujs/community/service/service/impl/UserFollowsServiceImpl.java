package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.UserFollowsMapper;
import com.github.lujs.community.api.model.pojo.UserFollows;
import com.github.lujs.community.api.service.IUserFollowsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class UserFollowsServiceImpl extends ServiceImpl<UserFollowsMapper, UserFollows> implements IUserFollowsService {

}
