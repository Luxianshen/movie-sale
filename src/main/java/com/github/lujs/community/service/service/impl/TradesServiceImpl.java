package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.TradesMapper;
import com.github.lujs.community.api.model.pojo.Trades;
import com.github.lujs.community.api.service.ITradesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class TradesServiceImpl extends ServiceImpl<TradesMapper, Trades> implements ITradesService {

}
