package com.github.lujs.community.service.service.impl;

import com.github.lujs.community.service.mapper.SchoolsMapper;
import com.github.lujs.community.api.model.pojo.Schools;
import com.github.lujs.community.api.service.ISchoolsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 *  服务实现类
 * @author joysim
 * @since 2020-03-27
 */
@Service
public class SchoolsServiceImpl extends ServiceImpl<SchoolsMapper, Schools> implements ISchoolsService {

}
