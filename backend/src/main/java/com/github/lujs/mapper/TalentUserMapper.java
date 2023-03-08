package com.github.lujs.mapper;

@Mapper
public interface TalentUserMapper extends BaseMapper<TalentUser> {

    /** 分页查询达人信息
     *
     * @param page
     * @param params
     * @return
     */
    Page<TalentUserPageDto> talentUserPage(Page<TalentUserPageDto> page, @Param("params") TalentUserPageQuery params);
}

