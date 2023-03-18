package com.github.lujs.model.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class WxLocation implements Serializable {

    /**
     * {
     *     "status":0,
     *     "message":"Success",
     *     "request_id":"da7d7c8b427347f282979e9fb0317446",
     *     "result":{
     *         "ip":"111.206.145.41",
     *         "location":{
     *             "lat":39.90469,
     *             "lng":116.40717
     *         },
     *         "ad_info":{
     *             "nation":"中国",
     *             "province":"北京市",
     *             "city":"北京市",
     *             "district":"",
     *             "adcode":110000
     *         }
     *     }
     * }
     */

    private String lat;
    private String lon;

    private Integer cityId;

    private String cityName;

    private String cityCode;

}
