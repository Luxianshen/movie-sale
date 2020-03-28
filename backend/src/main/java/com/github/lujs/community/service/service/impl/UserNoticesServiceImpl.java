package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.UserNoticesMapper;
import com.github.lujs.community.api.model.pojo.UserNotices;
import com.github.lujs.community.api.service.IUserNoticesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class UserNoticesServiceImpl extends ServiceImpl<UserNoticesMapper, UserNotices> implements IUserNoticesService {

}
