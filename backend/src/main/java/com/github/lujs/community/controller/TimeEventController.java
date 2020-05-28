package com.github.lujs.community.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.controller.request.PrimaryKeyRequest;
import com.github.lujs.commmon.model.vo.BaseRequest;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.github.lujs.community.api.model.pojo.TimeEvent;
import com.github.lujs.community.api.service.ITimeEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 前端控制器
 *
 * @author joysim
 * @since 2020-03-27
 */
@RestController
@RequestMapping("/community/timeEvent")
public class TimeEventController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(TimeEventController.class);

    @Resource
    private ITimeEventService targetService;

    /**
     * 获取详情
     *
     * @param request
     * @return
     */
    @RequestMapping("/get")
    public BaseResponse get(@Valid @RequestBody BaseRequest<PrimaryKeyRequest> request) {
        TimeEvent schools = targetService.getById(request.getData().getId());
        return successResponse(schools);
    }

    /**
     * 新增
     */
    @RequestMapping("/add")
    public BaseResponse add(@Valid @RequestBody BaseRequest<TimeEvent> request) {
        TimeEvent timeEvent = request.getData();
        timeEvent.init();
        timeEvent.setMonthDay(DateUtil.format(new Date(), "MM-dd"));
        timeEvent.setHourMin(DateUtil.thisHour(true) + ":" + DateUtil.thisMinute());
        boolean result = targetService.save(timeEvent);
        return baseResponse(result);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public BaseResponse update(@Valid @RequestBody BaseRequest<TimeEvent> request) {
        try {
            TimeEvent timeEvent = request.getData();
            timeEvent.setUpdateTime(new Date());
            boolean result = targetService.updateById(timeEvent);
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

        Map<String,List<TimeEvent>> result;

        QueryWrapper<TimeEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<TimeEvent> timeEvents = targetService.list(queryWrapper);
        result = sortByKey(timeEvents.stream().collect(Collectors.groupingBy(TimeEvent::getMonthDay)));
        return successResponse(result);
    }

    /**
     * 获取时间差
     * @return
     */
    @RequestMapping("/getTimePass")
    public BaseResponse getTimePass() {
        long passDay = DateUtil.between(new Date(),DateUtil.parse("2019-10-16"), DateUnit.DAY);
        return successResponse(passDay);
    }

    /**
     * map排序
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public <K extends Comparable<? super K>, V > Map<K, V> sortByKey(Map<K, V> map) {
        Map<K, V> result = new LinkedHashMap<>();

        map.entrySet().stream()
                .sorted(Map.Entry.<K, V>comparingByKey()
                        .reversed()).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}

