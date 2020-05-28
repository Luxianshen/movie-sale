package com.github.lujs.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.controller.request.PrimaryKeyRequest;
import com.github.lujs.commmon.model.vo.BaseRequest;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.github.lujs.community.api.model.pojo.TimePlan;
import com.github.lujs.community.api.service.ITimePlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 前端控制器
 *
 * @author joysim
 * @since 2020-03-27
 */
@RestController
@RequestMapping("/community/timePlan")
public class TimePlanController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(TimePlanController.class);

    @Resource
    private ITimePlanService targetService;

    /**
     * 获取详情
     *
     * @param request
     * @return
     */
    @RequestMapping("/get")
    public BaseResponse get(@Valid @RequestBody BaseRequest<PrimaryKeyRequest> request) {
        TimePlan timePlan = targetService.getById(request.getData().getId());
        return successResponse(timePlan);
    }

    /**
     * 新增
     */
    @RequestMapping("/add")
    public BaseResponse add(@Valid @RequestBody BaseRequest<TimePlan> request) {
        TimePlan timePlan = request.getData();
        timePlan.init();
        boolean result = targetService.save(timePlan);
        return baseResponse(result);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public BaseResponse update(@Valid @RequestBody BaseRequest<TimePlan> request) {
        try {
            TimePlan timePlan = request.getData();
            timePlan.setUpdateTime(new Date());
            boolean result = targetService.updateById(timePlan);
            return baseResponse(result);
        } catch (Exception ex) {
            logger.error("schoolsupdate -=- {}", ex.toString());
        }
        return null;
    }

    /**
     * 删除
     *
     * @param request
     * @return
     */
    @RequestMapping("/delete")
    public BaseResponse delete(@Valid @RequestBody BaseRequest<PrimaryKeyRequest> request) {
        boolean result = targetService.removeById(request.getData().getId());
        return baseResponse(result);
    }


    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public BaseResponse list() {

        Map<String,List<TimePlan>> result = new HashMap<>();

        QueryWrapper<TimePlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<TimePlan> timePlans = targetService.list(queryWrapper);
        //todo
        result = sortByKey(timePlans.stream().collect(Collectors.groupingBy(TimePlan::getName)));

        return successResponse(result);
    }


    public <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()
                        .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}

