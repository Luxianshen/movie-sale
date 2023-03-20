package com.github.lujs.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.lujs.model.pojo.WxLocation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.Charset;

@Slf4j
public class LocationUtil {

    @Value("cityJsonFilePath")
    private String cityJsonFilePath;

    public static WxLocation getLocation(HttpServletRequest request) {

        String rep = HttpUtil.get("https://restapi.amap.com/v3/ip?ip=" + request.getRemoteAddr() + "&key=1b85b506f2995558684577af0ac1a273");
        JSONObject result = JSONUtil.parseObj(rep);

        log.info(rep);
        WxLocation wxLocation = new WxLocation();
        wxLocation.setLon("113.34123");
        wxLocation.setLat("23.01234");
        wxLocation.setCityName("广州市");
        wxLocation.setCityCode("440100");
        wxLocation.setCityId(8);
        if ("1".equals(result.getStr("status")) && !"[]".equals(result.getStr("rectangle"))) {

            String[] rectangles = result.getStr("rectangle").split(";");
            if (rectangles.length == 2) {
                String[] location1 = rectangles[0].split(",");
                String[] location2 = rectangles[1].split(",");
                wxLocation.setLon(NumberUtil.roundStr((Double.parseDouble(location1[0]) + Double.parseDouble(location2[0])) / 2, 6));
                wxLocation.setLat(NumberUtil.roundStr((Double.parseDouble(location1[1]) + Double.parseDouble(location2[1])) / 2, 6));
                wxLocation.setCityName(result.getStr("city"));
                wxLocation.setCityCode(result.getStr("adcode"));
                wxLocation.setCityId(getCityId(result.getStr("adcode")));
            }

        }
        return wxLocation;
    }

    public static WxLocation getLocation(String lat, String lon) {

        String rep = HttpUtil.get("https://restapi.amap.com/v3/geocode/regeo?location=" + lon + "," + lat + "&poitype=&radius=100&extensions=base&batch=false&roadlevel=1&key=1b85b506f2995558684577af0ac1a273");
        JSONObject result = JSONUtil.parseObj(rep);
        WxLocation wxLocation = new WxLocation();
        wxLocation.setLon("113.34123");
        wxLocation.setLat("23.01234");
        wxLocation.setCityName("广州市");
        wxLocation.setCityCode("440100");
        if ("1".equals(result.getStr("status"))) {
            String province = result.getJSONObject("regeocode").getJSONObject("addressComponent").getStr("province");
            String city = result.getJSONObject("regeocode").getJSONObject("addressComponent").getStr("city");
            String adcode = result.getJSONObject("regeocode").getJSONObject("addressComponent").getStr("adcode");
            adcode = adcode.substring(0,4) +"00";
            wxLocation.setLat(lat);
            wxLocation.setLon(lon);
            wxLocation.setCityName(ObjectUtil.isEmpty(city) ? province : city);
            wxLocation.setCityCode(adcode);
            wxLocation.setCityId(getCityId(adcode));
        }
        return wxLocation;
    }


    private static Integer getCityId(String adcode){

        JSONArray array = JSONUtil.readJSONArray(new File("/Users/lulu/IdeaProjects/github/movie-sale/backend/doc/city.json"), Charset.defaultCharset());
        for (Object x : array) {
            JSONObject jsonObject = (JSONObject) x;
            if (jsonObject.getStr("cityCode").equals(adcode)){
                return jsonObject.getInt("cityId");
            }
        }
        return 8;
    }

}
