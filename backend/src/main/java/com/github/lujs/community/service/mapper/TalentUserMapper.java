package com.github.lujs.community.service.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.lujs.model.TalentUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TalentUserMapper extends BaseMapper<TalentUser> {

    /** 分页查询达人信息
     *
     * @param page
     * @param params
     * @return
     */
    //Page<TalentUserPageDto> talentUserPage(Page<TalentUserPageDto> page, @Param("params") TalentUserPageQuery params);
}

