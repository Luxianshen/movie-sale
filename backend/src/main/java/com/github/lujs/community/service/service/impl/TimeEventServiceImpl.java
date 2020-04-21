package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.TimeEventMapper;
import com.github.lujs.community.api.model.pojo.TimeEvent;
import com.github.lujs.community.api.service.ITimeEventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class TimeEventServiceImpl extends ServiceImpl<TimeEventMapper, TimeEvent> implements ITimeEventService {

}
