package com.github.lujs.community.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.lujs.community.api.model.pojo.Messages;
import com.github.lujs.community.api.service.IMessagesService;
import com.github.lujs.community.service.mapper.MessagesMapper;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class MessagesServiceImpl extends ServiceImpl<MessagesMapper, Messages> implements IMessagesService {

}
