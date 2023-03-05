package com.github.lujs.movie.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.lujs.model.Response;
import com.github.lujs.model.Seat;
import com.github.lujs.model.SeatData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/index")
public class IndexController {


    /**
     * 主页
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/movie")
    public String movieList(@PathParam("cityCode") String cityCode) {

        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cityCode=440100&page=1&limit=30&showType=1");
        String a = "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cityCode="+cityCode+"&page=5&limit=30&showType=2";
        return post;
    }

    /**
     * 电影详情
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/movieDetail/{cityCode}/{showId}")
    public String movieDetail(@PathVariable("cityCode") String cityCode, @PathVariable("showId") String showId) {
        String detail = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/detail", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&showId=" + showId);

        System.out.println(detail);
        return detail;
    }

    /**
     * 电影详情
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/schedule/{cityCode}/{showId}/{dateStr}")
    public String schedule(@PathVariable("cityCode") String cityCode, @PathVariable("showId") String showId,
                           @PathVariable("dateStr") String dateStr) {

        String schedule = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cityCode=440100&page=1&limit=10&showId="
                + showId + "&date=" + dateStr + "&area=&brand=&cinemaOrAddress=");

        return schedule;
    }

    /**
     * 电影详情
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/query/{cityCode}")
    public String query(@PathVariable("cityCode") String cityCode) {
        String query = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&cityCode=" + cityCode);
        return query;
    }

    /**
     * 电影院 详情
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/cinemas/{cinemaId}/{movieId}")
    public String cinemas(@PathVariable("cinemaId") String cinemaId, @PathVariable("movieId") String movieId) {
//888936  1430102
        //String cinemas = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/schedules-list-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cinemaId=" + cinemaId + "&movieId=" + movieId);
        //return cinemas;
        String result = JSONUtil.readJSON(new File("/Users/lulu/IdeaProjects/github/dubbo3_naocs2/doc/movieIndex.json"), Charset.defaultCharset()).toString();
        return result;
    }

    /**
     * 电影院 详情
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/seat/{cinemaName}/{showId}")
    public String seat(@PathVariable("cinemaName") String cinemaName,@PathVariable("showId") String showId) {

        //String seat = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/seat-plan/from-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cinemaName="+cinemaName+"&showId="+showId);
        //System.out.println(seat);
        String result = JSONUtil.readJSON(new File("/Users/lulu/IdeaProjects/github/dubbo3_naocs2/doc/seat.json"), Charset.defaultCharset()).toString();

        Response response = JSONUtil.toBean(result, Response.class);
        SeatData seatData = JSONUtil.toBean(((JSONObject) response.getData()).getStr("seatData"), SeatData.class);

        Map<String, List<Seat>> collect = seatData.getSeats().stream().collect(Collectors.groupingBy(Seat::getRowNo));

        response = new Response(200,"success",collect);
        return JSONUtil.toJsonStr(response);
    }

    @GetMapping("/house")
    public String house(@PathVariable("cityCode") String cityCode) {

        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=27164025&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&cityCode=440100");
        return post;
    }


}
