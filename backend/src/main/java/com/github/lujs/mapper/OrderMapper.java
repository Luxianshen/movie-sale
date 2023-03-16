package com.github.lujs.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.lujs.model.page.CzOrderPage;
import com.github.lujs.model.pojo.Order;
import org.apache.ibatis.annotations.Param;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:54
 */
public interface OrderMapper extends BaseMapper<Order> {

    IPage<CzOrderPage> myOrderPage(Page<CzOrderPage> page, @Param("userId") Long userId);

}
