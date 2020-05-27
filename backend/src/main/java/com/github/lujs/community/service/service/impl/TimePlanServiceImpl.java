package com.github.lujs.community.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.lujs.community.api.model.pojo.TimePlan;
import com.github.lujs.community.api.service.ITimePlanService;
import com.github.lujs.community.service.mapper.TimePlanMapper;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class TimePlanServiceImpl extends ServiceImpl<TimePlanMapper, TimePlan> implements ITimePlanService {

}
