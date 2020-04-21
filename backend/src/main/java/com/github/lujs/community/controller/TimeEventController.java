package com.github.lujs.community.controller;

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
            TimeEvent schools = request.getData();
            schools.setUpdateTime(new Date());
            boolean result = targetService.updateById(schools);
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

        //Map<String,List<TimeEvent>> reslut = new HashMap<>();

        QueryWrapper<TimeEvent> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        List<TimeEvent> timeEvents = targetService.list(queryWrapper);

        Map<String,List<TimeEvent>> map = timeEvents.stream().collect(Collectors.groupingBy(TimeEvent::getMonthDay));

        LinkedHashMap result = new LinkedHashMap();
        ListIterator<Map.Entry<String,List<TimeEvent>>> i = new ArrayList<Map.Entry<String,List<TimeEvent>>>(map.entrySet()).listIterator(map.size());

        while(i.hasPrevious()) {
            Map.Entry<String, List<TimeEvent>> entry=i.previous();
            result.put(entry.getKey(),entry.getValue());
        }

        return successResponse(result);
    }
}

