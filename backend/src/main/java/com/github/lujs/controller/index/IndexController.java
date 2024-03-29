package com.github.lujs.controller.index;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.lujs.commmon.CzToken;
import com.github.lujs.commmon.annotation.Token;
import com.github.lujs.model.Response;
import com.github.lujs.model.pojo.Seat;
import com.github.lujs.model.pojo.SeatData;
import com.github.lujs.model.pojo.SeatNew;
import com.github.lujs.model.pojo.WxLocation;
import com.github.lujs.model.query.NormalQuery;
import com.github.lujs.util.LocationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    private static final String DEFAULT_PREFIX = "index:";

    private static final String FAIL_STR = "{\"code\":-100,\"message\":\"系统异常，请稍后再试！\",\"success\":false}";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 主页
     */
    @GetMapping("/movie")
    public String movieList(HttpServletRequest request) {

        WxLocation location = LocationUtil.getLocation(request);
        String cacheKey = DEFAULT_PREFIX + location.getCityId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cityCode=&page=1&limit=30&showType=1");
        if (!post.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, post, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        return post;
    }


    /**
     * 电影详情
     */
    @GetMapping("/movieDetail/{showId}")
    public String movieDetail(@Token CzToken token, @PathVariable("showId") String showId) {

        String cacheKey = DEFAULT_PREFIX + token.getCityId() + showId + token.getId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String detail = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/detail", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLat() + "&cityId=" + token.getCityId() + "&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci="+token.getCityId()+"&showId=" + showId);
        if (!detail.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, detail, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        return detail;
    }

    /**
     * 电影详情
     */
    @PostMapping("/schedule/{showId}/{dateStr}")
    public String schedule(@Token CzToken token, @PathVariable("showId") String showId,
                           @PathVariable("dateStr") String dateStr, @RequestBody NormalQuery query) {

        String cacheKey = DEFAULT_PREFIX + token.getCityId() + showId + dateStr + query.getArea() + query.getBrand() + query.getOffset() + token.getId();

        String schedule;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            schedule = redisTemplate.opsForValue().get(cacheKey);
        else {
            schedule = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=" + token.getCityId() + "&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci="+token.getCityId()+"&cityCode=&page=" + query.getOffset() + "&limit=20&showId="
                    + showId + "&date=" + dateStr + "&area=" + query.getArea() + "&brand=" + query.getBrand() + "&cinemaOrAddress=");

            if (!schedule.equals(FAIL_STR))
                redisTemplate.opsForValue().set(cacheKey, schedule, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        }

        return schedule;
    }

    /**
     * 电影详情
     */
    @GetMapping("/query")
    public String query(@Token CzToken token) {
        String cacheKey = DEFAULT_PREFIX + "query:" + token.getCityId() + token.getId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String query = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=" + token.getCityId() + "&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&cityCode="+token.getCityCode());
        if (!query.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, query, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        return query;
    }

    /**
     * 电影院 详情
     */
    @GetMapping("/cinemas/{cinemaId}/{movieId}")
    public String cinemas(@Token CzToken token, @PathVariable("cinemaId") String cinemaId, @PathVariable("movieId") String movieId) {

        String cacheKey = DEFAULT_PREFIX + cinemaId + movieId + token.getId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        if ("123".equals(movieId)) movieId = "";
        String cinemas = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/schedules-list-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=" + token.getCityId() + "&cinemaId=" + cinemaId + "&movieId=" + movieId);
        if (!cinemas.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, cinemas, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        return cinemas;
    }

    /**
     * 电影院 详
     */
    @GetMapping("/seat/{cinemaName}/{showId}")
    public String seat(@Token CzToken token,@PathVariable("cinemaName") String cinemaName, @PathVariable("showId") String showId) {

        String seat = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/seat-plan/from-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=" + token.getCityId() + "&cinemaName=" + cinemaName + "&showId=" + showId);
        Response response = JSONUtil.toBean(seat, Response.class);
        SeatData seatData = JSONUtil.toBean(((JSONObject) response.getData()).getStr("seatData"), SeatData.class);
        Map<String, List<Seat>> collect = seatData.getSeats().stream().collect(Collectors.groupingBy(Seat::getRowNo));
        List<SeatNew> dataList = new ArrayList<>();
        collect.forEach((key, value) -> value.forEach(item -> {
            String type = "N".equals(item.getStatus()) ? "0" : "0-2";
            String flag = 0 == item.getLovestatus() ? "0" : "1";
            dataList.add(new SeatNew(item.getSeatId(), item.getSeatNo(), item.getRowNo(), item.getColumnNo(), type, flag));
        }));

        return JSONUtil.toJsonStr(dataList);
    }


    @GetMapping("/house")
    public String house(@Token CzToken token, HttpServletRequest request) {

        WxLocation location = LocationUtil.getLocation(request);
        String cacheKey = DEFAULT_PREFIX + "house" + location.getCityId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&cityCode=" + token.getCityCode());
        if (!post.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, post, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        return post;
    }

    @PostMapping("/cinemaList")
    public String cinemaList(@Token CzToken token, @RequestBody NormalQuery query) {

        String cacheKey = DEFAULT_PREFIX + "cinemaList" + query.getOffset() + query.getArea() + query.getBrand() + token.getId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);

        String cinemaList = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/cinema/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=" + token.getCityId() + "&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&cityCode=440100&ci="+token.getCityId()+"&page=" + query.getOffset() + "&limit=20&area=" + query.getArea() + "&brand=" + query.getBrand());
        if (!cinemaList.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, cinemaList, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        return cinemaList;
    }


    /**
     * 搜索
     */
    @GetMapping("/search/{key}")
    public String search(HttpServletRequest request, @Token CzToken token, @PathVariable("key") String key) {

        String cacheKey = DEFAULT_PREFIX + "search" + key + token.getId();
        if (Boolean.TRUE.equals(redisTemplate.hasKey(cacheKey)))
            return redisTemplate.opsForValue().get(cacheKey);
        String search = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/cinema/queryCinema", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=" + token.getLat() + "&longitude=" + token.getLon() + "&cityId=" + token.getCityId() + "&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci="+token.getCityId()+"&kw=" + key);
        if (!search.equals(FAIL_STR))
            redisTemplate.opsForValue().set(cacheKey, search, DateUtil.between(new Date(), DateUtil.endOfDay(new Date()), DateUnit.MINUTE), TimeUnit.MINUTES);
        return search;
    }

    /**
     * 地址
     * https://yp-api.taototo.cn/yp-api/movie/address/get
     * lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=23.15653812&longitude=113.3893937&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=
     */

    @GetMapping("/getNowCity")
    public String getNowCity(HttpServletRequest request) {
        return LocationUtil.getLocation(request).getCityName();
    }
}
