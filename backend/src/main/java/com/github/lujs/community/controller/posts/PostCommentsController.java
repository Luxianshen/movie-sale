package com.github.lujs.community.controller.posts;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.controller.request.PrimaryKeyRequest;
import com.github.lujs.commmon.model.vo.BaseRequest;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.github.lujs.commmon.query.PageQuery;
import com.github.lujs.community.api.model.pojo.PostComments;
import com.github.lujs.community.api.model.query.PostCommentsQuery;
import com.github.lujs.community.api.service.IPostCommentsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 *  前端控制器
 * @author joysim
 * @since 2020-03-27
 */
@RestController
@RequestMapping("/community/postComments")
public class PostCommentsController extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(PostCommentsController.class);

    @Resource
    private IPostCommentsService targetService;

    /**
     * 获取详情
     * @param request
     * @return
     */
    @RequestMapping("/get")
    public BaseResponse get(@Valid @RequestBody BaseRequest<PrimaryKeyRequest> request) {
        PostComments postComments = targetService.getById(request.getData().getId());
            return successResponse(postComments);
    }

    /**
    * 新增
    */
    @RequestMapping("/add")
    public BaseResponse add(@Valid @RequestBody BaseRequest<PostComments> request) {
            boolean result = targetService.save(request.getData());
            return baseResponse(result);
    }
    /**
    * 修改
    */
    @RequestMapping("/update")
    public BaseResponse update(@Valid @RequestBody BaseRequest<PostComments> request){
        try{
            PostComments postComments =request.getData();
            postComments.setUpdateTime(new Date());
            boolean result= targetService.updateById(postComments);
            return baseResponse(result);
        }catch(Exception ex){
            logger.error("postCommentsupdate -=- {}",ex.toString());
        }
        return null;
    }

    /**
     * 删除
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
    * @param request
    * @return
    */
    @RequestMapping("/page")
    public BaseResponse page(@RequestBody BaseRequest<PageQuery<PostComments, PostCommentsQuery>> request) {
        PageQuery<PostComments, PostCommentsQuery> page = request.getData();
        PostCommentsQuery query = page.getParams();
        QueryWrapper<PostComments> wrapper = new QueryWrapper<>();
        /*
        if (null != query.getName()) {
        wrapper.eq("name", query.getName());
        }
        if (null != query.getState()) {
        wrapper.eq("state", query.getState());
        }
        */
        targetService.page(page, wrapper);
        return successResponse(page);
    }
}

