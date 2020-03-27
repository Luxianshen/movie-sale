package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.UserMessagesMapper;
import com.github.lujs.community.api.model.pojo.UserMessages;
import com.github.lujs.community.api.service.IUserMessagesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class UserMessagesServiceImpl extends ServiceImpl<UserMessagesMapper, UserMessages> implements IUserMessagesService {

}
