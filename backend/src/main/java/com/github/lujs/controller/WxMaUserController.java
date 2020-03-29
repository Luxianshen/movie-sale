package com.github.lujs.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.github.lujs.community.api.model.pojo.Users;
import com.github.lujs.community.api.service.IUsersService;
import com.github.lujs.config.WxMaConfiguration;
import com.github.lujs.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import me.chanjar.weixin.common.error.WxErrorException;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RestController
@RequestMapping("/wx/user/{appid}")
public class WxMaUserController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private IUsersService usersService;

    /**
     * 登陆接口
     */
    @GetMapping("/login")
    public BaseResponse login(@PathVariable String appid, String code) {
        if (StringUtils.isBlank(code)) {
            return failedResponse("empty jscode");
        }

        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            this.logger.info(session.getSessionKey());
            this.logger.info(session.getOpenid());
            Map<String, Object> result = new HashMap<>();
            QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("openId", session.getOpenid());
            Users users = usersService.getOne(queryWrapper);
            if (users == null) {
                users = new Users();
                users.init();
                users.setOpenId(session.getOpenid());
                usersService.save(users);
            }
            //TODO 可以增加自己的逻辑，关联业务相关数据
            result.put("user",users);
            result.put("token","null");
            return successResponse(result);
        } catch (WxErrorException e) {
            this.logger.error(e.getMessage(), e);
            return failedResponse(e.toString());
        }
    }

    /**
     * <pre>
     * 获取用户信息接口
     * </pre>
     */
    @GetMapping("/info")
    public String info(@PathVariable String appId, String sessionKey,
                       String signature, String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appId);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }
        // 解密用户信息
        WxMaUserInfo userInfo = wxService.getUserService().getUserInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(userInfo);
    }

    /**
     * <pre>
     * 获取用户绑定手机号信息
     * </pre>
     */
    @GetMapping("/phone")
    public String phone(@PathVariable String appid, String sessionKey, String signature,
                        String rawData, String encryptedData, String iv) {
        final WxMaService wxService = WxMaConfiguration.getMaService(appid);

        // 用户信息校验
        if (!wxService.getUserService().checkUserInfo(sessionKey, rawData, signature)) {
            return "user check failed";
        }

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = wxService.getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        return JsonUtils.toJson(phoneNoInfo);
    }

}
