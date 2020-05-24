package com.github.lujs.community.controller.posts;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.controller.request.PrimaryKeyRequest;
import com.github.lujs.commmon.model.vo.BaseRequest;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.github.lujs.commmon.query.PageQuery;
import com.github.lujs.community.api.model.pojo.Posts;
import com.github.lujs.community.api.model.query.PostsQuery;
import com.github.lujs.community.api.service.IPostCommentsService;
import com.github.lujs.community.api.service.IPostLikesService;
import com.github.lujs.community.api.service.IPostsService;
import com.github.lujs.community.api.service.IUsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

/**
 *  前端控制器
 * @author joysim
 * @since 2020-03-27
 */
@RestController
@RequestMapping("/community/posts")
public class PostsController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(PostsController.class);

    @Resource
    private IPostsService targetService;

    @Resource
    private IUsersService usersService;

    @Resource
    private IPostLikesService postLikesService;

    @Resource
    private IPostCommentsService postCommentsService;

    /**
     * 获取详情
     * @param request
     * @return
     */
    @RequestMapping("/get")
    public BaseResponse get(@Valid @RequestBody BaseRequest<PrimaryKeyRequest> request) {
        Posts posts = targetService.getById(request.getData().getId());
            return successResponse(posts);
    }

    /**
    * 新增
    */
    @RequestMapping("/add")
    public BaseResponse add(@Valid @RequestBody BaseRequest<Posts> request) {
            boolean result = targetService.save(request.getData());
            return baseResponse(result);
    }
    /**
    * 修改
    */
    @RequestMapping("/update")
    public BaseResponse update(@Valid @RequestBody BaseRequest<Posts> request){
        try{
            Posts posts =request.getData();
            posts.setUpdateTime(new Date());
            boolean result= targetService.updateById(posts);
            return baseResponse(result);
        }catch(Exception ex){
            logger.error("postsupdate -=- {}",ex.toString());
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
    public BaseResponse page(@RequestBody BaseRequest<PageQuery<Posts, PostsQuery>> request) {
        //返回个map 包含用户信息 文章内容 喜欢的 评论
        List<Object> list = new ArrayList<>();

        PageQuery<Posts, PostsQuery> page = request.getData();
        PostsQuery query = page.getParams();
        QueryWrapper<Posts> wrapper = new QueryWrapper<>();
        wrapper.eq("is_recommend", 1);
        wrapper.orderByDesc("create_time");
        targetService.page(page, wrapper);
        page.getRecords().forEach(x->{
            Map<String,Object> result = new HashMap<>();
            result.put("user",usersService.getById(x.getUserId()));
            result.put("post",x);
            result.put("likers",postLikesService.getByPostId(x.getId()));
            result.put("comments",postCommentsService.getByPostId(x.getId()));
            list.add(result);
        });
        return successResponse(list);
    }

    /**
     * 分页查询
     * @param request
     * @return
     */
    @PostMapping("/recommend/new")
    public BaseResponse recommend() {

        QueryWrapper<Posts> wrapper = new QueryWrapper<>();
        wrapper.eq("isRecommend", 1);

        return successResponse(targetService.list(wrapper));
    }

    /**
     * 查询关注
     * @param
     * @return
     */
    @PostMapping("/follow")
    public BaseResponse follow(@RequestBody BaseRequest<PageQuery<Posts, PostsQuery>> request) {

        return successResponse(targetService.list());
    }


    /**
     * 发布图文
     * @param request
     * @return
     */
    @RequestMapping("/release")
    public BaseResponse release(@Valid @RequestBody BaseRequest<Posts> request) {
        Posts posts = request.getData();
        posts.init();
        boolean result = targetService.save(posts);
        return baseResponse(result);
    }

}

