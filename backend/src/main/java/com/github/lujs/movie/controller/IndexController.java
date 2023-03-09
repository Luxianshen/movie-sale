package com.github.lujs.movie.controller;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.lujs.model.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.util.IPAddressUtil;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
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

        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=afab2bd7a7984fc18231f8619ce7a11c&v=&isCouponPop=&ci=8&cityCode=440100&page=1&limit=30&showType=1");
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
        String detail = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/film/detail", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&showId=" + showId);

        System.out.println(detail);
        return detail;
    }

    /**
     * 电影详情
     *
     * @param cityCode
     * @return
     */
    @GetMapping("/schedule/{cityCode}/{showId}/{dateStr}/{page}")
    public String schedule(@PathVariable("cityCode") String cityCode, @PathVariable("showId") String showId,
                           @PathVariable("dateStr") String dateStr, @PathVariable("page") Integer page) {

        String schedule = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&cityCode=440100&page=" + page + "&limit=20&showId="
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
        String query = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&cityCode=" + cityCode);
        return query;
    }

    /**
     * 电影院 详情
     *
     * @return
     */
    @GetMapping("/cinemas/{cinemaId}/{movieId}")
    public String cinemas(@PathVariable("cinemaId") String cinemaId, @PathVariable("movieId") String movieId) {

        String cinemas = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/schedule/schedules-list-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&cinemaId=" + cinemaId + "&movieId=" + movieId);
        return cinemas;
    }

    /**
     * 电影院 详
     *
     * @return
     */
    @GetMapping("/seat/{cinemaName}/{showId}")
    public String seat(@PathVariable("cinemaName") String cinemaName, @PathVariable("showId") String showId) {

        String seat = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/seat-plan/from-qmm", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&ci=8&cinemaName=" + cinemaName + "&showId=" + showId);
        System.out.println(seat);
        Response response = JSONUtil.toBean(seat, Response.class);
        SeatData seatData = JSONUtil.toBean(((JSONObject) response.getData()).getStr("seatData"), SeatData.class);
        Map<String, List<Seat>> collect = seatData.getSeats().stream().collect(Collectors.groupingBy(Seat::getRowNo));
        response = new Response(200, "success", collect);
        return JSONUtil.toJsonStr(response);
    }

    @GetMapping("/house")
    public String house(@PathVariable("cityCode") String cityCode) {

        String post = HttpUtil.post("https://yp-api.taototo.cn/yp-api/movie/region/query", "lat=&lng=&mode=qmm&app_key=&domainName=https%3A%2F%2Fgw.taototo.cn%2F&token=&platformUUID=123&latitude=&longitude=&cityId=8&evnType=h5&envType=h5&userUUID=123&v=&isCouponPop=&cityCode=440100");
        return post;
    }

    @GetMapping("getCity")
    public Result<WxLocation> getCity(HttpServletRequest request){

        String rep = HttpUtil.get("https://apis.map.qq.com/ws/location/v1/ip?ip="+request.getRemoteAddr()+"&key=5EPBZ-Y6563-EEG3O-3GBDE-G3XZO-AZBCI");
        JSONObject jsonObject = JSONUtil.parseObj(rep);
        WxLocation wxLocation = new WxLocation();
        wxLocation.setLon("113.34");
        wxLocation.setLat("23.01");
        wxLocation.setCityName("广州市");
        wxLocation.setCityCode("440100");
        if (jsonObject.getStr("status").equals("0")){
            JSONObject result = JSONUtil.parseObj(jsonObject.getStr("result"));
            wxLocation.setLat(JSONUtil.parseObj(result.getStr("location")).getStr("lat"));
            wxLocation.setLat(JSONUtil.parseObj(result.getStr("location")).getStr("lon"));
            wxLocation.setCityName(JSONUtil.parseObj(result.getStr("ad_info")).getStr("city"));
            wxLocation.setCityCode(JSONUtil.parseObj(result.getStr("ad_info")).getStr("adcode"));
        }

        return Result.succeed(wxLocation);

    }


}
