package com.github.lujs.community.controller.topic;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.controller.request.PrimaryKeyRequest;
import com.github.lujs.commmon.model.vo.BaseRequest;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.github.lujs.commmon.query.PageQuery;
import com.github.lujs.community.api.model.pojo.TopicFollows;
import com.github.lujs.community.api.model.pojo.Topics;
import com.github.lujs.community.api.model.query.TopicsQuery;
import com.github.lujs.community.api.service.ITopicFollowsService;
import com.github.lujs.community.api.service.ITopicsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 前端控制器
 *
 * @author joysim
 * @since 2020-03-27
 */
@RestController
@RequestMapping("/community/topics")
public class TopicsController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(TopicsController.class);

    @Resource
    private ITopicsService targetService;

    @Resource
    private ITopicFollowsService topicFollowsService;

    /**
     * 获取详情
     *
     * @param request
     * @return
     */
    @RequestMapping("/get")
    public BaseResponse get(@Valid @RequestBody BaseRequest<PrimaryKeyRequest> request) {
        Topics topics = targetService.getById(request.getData().getId());
        return successResponse(topics);
    }

    /**
     * 新增
     */
    @RequestMapping("/add")
    public BaseResponse add(@Valid @RequestBody BaseRequest<Topics> request) {
        Topics topics = request.getData();
        topics.init();
        boolean result = targetService.save(request.getData());
        return baseResponse(result);
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public BaseResponse update(@Valid @RequestBody BaseRequest<Topics> request) {
        try {
            Topics topics = request.getData();
            topics.setUpdateTime(new Date());
            boolean result = targetService.updateById(topics);
            return baseResponse(result);
        } catch (Exception ex) {
            logger.error("topicsupdate -=- {}", ex.toString());
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
    @RequestMapping("/page")
    public BaseResponse page(@RequestBody BaseRequest<PageQuery<Topics, TopicsQuery>> request) {
        PageQuery<Topics, TopicsQuery> page = request.getData();
        TopicsQuery query = page.getParams();
        QueryWrapper<Topics> wrapper = new QueryWrapper<>();
        return successResponse(targetService.list(wrapper));
    }

    /**
     * 获取user关注的话题
     *
     * @param userId
     * @return
     */
    @RequestMapping("/list/user/{userId}")
    public BaseResponse get(@PathVariable("userId") Long userId) {
        List<Topics> topics = new ArrayList<>();
        if (userId != null) {
            QueryWrapper<TopicFollows> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userId", userId);
            List<Long> topicsIds = topicFollowsService.list().stream().map(TopicFollows::getTopicId).collect(Collectors.toList());
            if (topicsIds.size() > 0) {
                QueryWrapper<Topics> queryTopicWrapper = new QueryWrapper<>();
                queryTopicWrapper.in("id", topicsIds);
                topics = targetService.list(queryTopicWrapper);
                return successResponse(topics);
            }
        }
        return successResponse(topics);
    }
}

