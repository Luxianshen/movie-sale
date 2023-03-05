package com.github.lujs.community.controller.posts;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.controller.request.PrimaryKeyRequest;
import com.github.lujs.commmon.exception.status.GlobalStatusCode;
import com.github.lujs.commmon.model.vo.BaseRequest;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.github.lujs.commmon.query.PageQuery;
import com.github.lujs.community.api.model.pojo.Posts;
import com.github.lujs.community.api.model.query.PostsQuery;
import com.github.lujs.community.api.service.IPostCommentsService;
import com.github.lujs.community.api.service.IPostLikesService;
import com.github.lujs.community.api.service.IPostsService;
import com.github.lujs.community.api.service.IUsersService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 前端控制器
 *
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
     *
     * @param request
     * @return
     */
    @RequestMapping("/get")
    public BaseResponse get(@Valid @RequestBody BaseRequest<PrimaryKeyRequest> request) {
        Map<String, Object> result = new HashMap<>();
        Posts posts = targetService.getById(request.getData().getId());
        result.put("post", posts);
        return successResponse(result);
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
    public BaseResponse update(@Valid @RequestBody BaseRequest<Posts> request) {
        try {
            Posts posts = request.getData();
            posts.setUpdateTime(new Date());
            boolean result = targetService.updateById(posts);
            return baseResponse(result);
        } catch (Exception ex) {
            logger.error("postsupdate -=- {}", ex.toString());
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
    public BaseResponse page(@RequestBody BaseRequest<PageQuery<Posts, PostsQuery>> request) {
        //返回个map 包含用户信息 文章内容 喜欢的 评论
        List<Object> list = new ArrayList<>();

        PageQuery<Posts, PostsQuery> page = request.getData();
        PostsQuery query = page.getParams();
        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        if (query.getType() != null) {
            queryWrapper.eq("pos_type", query.getType());
        }
        if (query.getIsRefresh() == 1 && query.getBegin() != null) {
            queryWrapper.ge("create_time", query.getBegin());
        }
        /*if(query.getIsRecommend() == 1){
            queryWrapper.eq("is_recommend", 1);
        }*/
        if (query.getUserId() != null) {
            queryWrapper.eq("user_id", query.getUserId());
        }
        queryWrapper.orderByDesc("create_time");
        targetService.page(page, queryWrapper);
        page.getRecords().forEach(x -> {
            Map<String, Object> result = new HashMap<>();
            result.put("user", usersService.getById(x.getUserId()));
            result.put("post", x);
            result.put("likers", postLikesService.getByPostId(x.getId()));
            result.put("comments", postCommentsService.getByPostId(x.getId()));
            list.add(result);
        });
        return successResponse(list);
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    @PostMapping("/recommend/new")
    public BaseResponse recommend(@RequestBody BaseRequest<PageQuery<Posts, PostsQuery>> request) {

        PageQuery<Posts, PostsQuery> page = request.getData();
        PostsQuery postsQuery = page.getParams();

        QueryWrapper<Posts> queryWrapper = new QueryWrapper<>();
        if (postsQuery.getType() != null) {
            queryWrapper.eq("post_type", postsQuery.getType());
        }
        queryWrapper.eq("isRecommend", 1);
        return successResponse(targetService.page(page, queryWrapper));
    }

    /**
     * 查询关注
     *
     * @param
     * @return
     */
    @PostMapping("/follow")
    public BaseResponse follow(@RequestBody BaseRequest<PageQuery<Posts, PostsQuery>> request) {
        return successResponse(targetService.list());
    }


    /**
     * 发布图文
     *
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

    /**
     * 校验文章
     */
    @RequestMapping("/analyse")
    public BaseResponse analyse(@Valid @RequestBody BaseRequest<Posts> request) {
        Posts posts = request.getData();
        //检查文章地址 获取文章html
        String urlRegex = "https?://(mp.weixin.qq.com)[-A-Za-z0-9+&@#/%=~_|]*";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher mac = pattern.matcher(posts.getLink());
        if (mac.matches()) {
            getArticleInfo(posts);
            return successResponse(posts);
        }
        return failedResponse(null);
    }

    /**
     * 分享文章
     */
    @RequestMapping("/article")
    public BaseResponse article(@Valid @RequestBody BaseRequest<Posts> request) {
        Posts posts = request.getData();
        //检查文章地址 获取文章html
        String urlRegex = "https?://(mp.weixin.qq.com)[-A-Za-z0-9+&@#/%=~_|]*";
        Pattern pattern = Pattern.compile(urlRegex);
        Matcher mac = pattern.matcher(posts.getLink());
        if (mac.matches()) {
            posts.init();
            getArticleInfo(posts);
            if (StringUtils.isNotEmpty(posts.getArticleTitle())) {
                return successResponse(targetService.save(posts));
            }
        }
        return failedResponse(GlobalStatusCode.FAILED);
    }

    private void getArticleInfo(Posts posts) {
        try {
            URL url = new URL(posts.getLink());
            BufferedReader in = new BufferedReader(new InputStreamReader(url
                    .openStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                if (line.contains("<meta property=\"og:title\"")) {
                    String[] lines = line.split("\"");
                    posts.setArticleTitle(lines[3]);
                }
                if (line.contains("<meta property=\"og:image\"")) {
                    String[] lines = line.split("\"");
                    posts.setArticleImg(lines[3]);
                }
                if (StringUtils.isNotEmpty(posts.getArticleImg())) {
                    break;
                }
            }
            in.close();
        } catch (Exception ex) {
            throw new RuntimeException("解析地址出错");
        }
    }

}

