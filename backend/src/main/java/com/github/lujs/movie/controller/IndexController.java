package com.github.lujs.movie.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.lujs.commmon.CzToken;
import com.github.lujs.commmon.annotation.Token;
import com.github.lujs.model.Response;
import com.github.lujs.model.Seat;
import com.github.lujs.model.SeatData;
import com.github.lujs.model.WxLocation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/index")
public class IndexController {

    private static final String DEFAULT_PREFIX = "index:";

    private static final String FAIL_STR = "{\"code\":-100,\"message\":\"系统异常，请稍后再试！\",\"success\":false}";

    @Resource
    private StringRedisTemplate redisTemplate;

    /**
     * 主页
     */
    @GetMapping("/movie")
    public String movieList(HttpServletRequest request) {

        WxLocation location = getCity(request);
        String cacheKey = DEFAULT_PREFIX + location.getCityCode();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cityCode=" + location.getCityCode() + "&page=1&limit=30&showType=1");
        if (!post.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, post);
        return post;
    }

    private WxLocation getCity(HttpServletRequest request) {

        String rep = HttpUtil.get("https://apis.map.qq.com/ws/location/v1/ip?ip=" + request.getRemoteAddr() + "&key=5EPBZ-Y6563-EEG3O-3GBDE-G3XZO-AZBCI");
        JSONObject jsonObject = JSONUtil.parseObj(rep);
        WxLocation wxLocation = new WxLocation();
        wxLocation.setLon("113.34");
        wxLocation.setLat("23.01");
        wxLocation.setCityName("广州市");
        wxLocation.setCityCode("440100");
        if ("0".equals(jsonObject.getStr("status"))) {
            JSONObject result = JSONUtil.parseObj(jsonObject.getStr("result"));
            wxLocation.setLat(JSONUtil.parseObj(result.getStr("location")).getStr("lat"));
            wxLocation.setLat(JSONUtil.parseObj(result.getStr("location")).getStr("lon"));
            wxLocation.setCityName(JSONUtil.parseObj(result.getStr("ad_info")).getStr("city"));
            wxLocation.setCityCode(JSONUtil.parseObj(result.getStr("ad_info")).getStr("adcode"));
        }
        return wxLocation;
    }

    /**
     * 电影详情
     */
    @GetMapping("/movieDetail/{showId}")
    public String movieDetail(@Token CzToken token, @PathVariable("showId") String showId) {

        String cacheKey = DEFAULT_PREFIX + token.getCityCode() + showId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String detail = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/detail", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLat() + "&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&showId=" + showId);
        if (!detail.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, detail);
        return detail;
    }

    /**
     * 电影详情
     */
    @GetMapping("/schedule/{showId}/{dateStr}/{page}")
    public String schedule(@Token CzToken token, @PathVariable("showId") String showId,
                           @PathVariable("dateStr") String dateStr, @PathVariable("page") Integer page) {

        String cacheKey = DEFAULT_PREFIX + token.getCityCode() + showId + dateStr + page;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String schedule = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&cityCode=" + token.getCityCode() + "&page=" + page + "&limit=20&showId="
                + showId + "&date=" + dateStr + "&area=&brand=&cinemaOrAddress=");
        if (!schedule.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, schedule);
        return schedule;
    }

    /**
     * 电影详情
     */
    @GetMapping("/query")
    public String query(@Token CzToken token) {
        String cacheKey = DEFAULT_PREFIX + "query:" + token.getCityCode();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String query = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&cityCode=" + token.getCityCode());
        if (!query.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, query);
        return query;
    }

    /**
     * 电影院 详情
     */
    @GetMapping("/cinemas/{cinemaId}/{movieId}")
    public String cinemas(@Token CzToken token, @PathVariable("cinemaId") String cinemaId, @PathVariable("movieId") String movieId) {

        String cacheKey = DEFAULT_PREFIX + cinemaId + movieId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String cinemas = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/schedules-list-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&cinemaId=" + cinemaId + "&movieId=" + movieId);
        if (!cinemas.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, cinemas);
        return cinemas;
    }

    /**
     * 电影院 详
     */
    @GetMapping("/seat/{cinemaName}/{showId}")
    public String seat(@PathVariable("cinemaName") String cinemaName, @PathVariable("showId") String showId) {

        String cacheKey = DEFAULT_PREFIX + cinemaName + showId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);

        String seat = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/seat-plan/from-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&cinemaName=" + cinemaName + "&showId=" + showId);
        Response response = JSONUtil.toBean(seat, Response.class);
        SeatData seatData = JSONUtil.toBean(((JSONObject) response.getData()).getStr("seatData"), SeatData.class);
        Map<String, List<Seat>> collect = seatData.getSeats().stream().collect(Collectors.groupingBy(Seat::getRowNo));
        response = new Response(200, "success", collect);
        String result = JSONUtil.toJsonStr(response);
        if (!seat.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, result);
        return result;
    }

    @GetMapping("/house")
    public String house(@PathVariable("cityCode") String cityCode) {

        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&cityCode=440100");
        return post;
    }


}
