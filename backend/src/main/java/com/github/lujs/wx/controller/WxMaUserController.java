package com.github.lujs.wx.controller;

import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.lujs.commmon.CzToken;
import com.github.lujs.commmon.annotation.Token;
import com.github.lujs.commmon.config.WxMaConfiguration;
import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.model.vo.BaseRequest;
import com.github.lujs.community.service.mapper.TalentThirdMapper;
import com.github.lujs.community.service.mapper.TalentUserMapper;
import com.github.lujs.model.*;
import com.github.lujs.service.CzTokenService;
import com.github.lujs.wx.controller.request.UserMaLoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

/**
 * 微信小程序用户接口
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@RestController
@RequestMapping("/wx")
public class WxMaUserController extends BaseController {

    @Resource
    private CzTokenService tokenService;

    @Resource
    private TalentThirdMapper talentThirdMapper;

    @Resource
    private TalentUserMapper talentUserMapper;


    @Value("${wx.miniapp.configs[0].appid}")
    private String appId;


    /**
     * 小程序用户登录
     *
     * @param request
     * @return
     */
    @GetMapping("/maLogin/{code}")
    public Result maLogin(@PathVariable("code") String code) {

        String sessionKey;
        try {
            WxMaJscode2SessionResult session = WxMaConfiguration.getMaService(appId).getUserService().getSessionInfo(code);

            Assert.notNull(session);
            Assert.notNull(session.getOpenid());
            log.info("openId:{} unionId:{} sessionKey:{}", session.getOpenid(), session.getUnionid(), session.getSessionKey());
            sessionKey = session.getSessionKey();

            // 获取或保存用户及帐户，注意：创建用户，在bindPhone时也可能判断创建
            TalentThird newUser = new TalentThird().setOpenId(session.getOpenid()).setUnionId(session.getUnionid()).setAppId(appId);
            UserDto userDto = this.getOrCreateUser(newUser);
            //返回sessionKey 置换info
            if (sessionKey != null)
                userDto.setSessionKey(sessionKey);

            return Result.succeed(tokenService.createCzToken(userDto));
        } catch (Exception e) {
            log.error("wx小程序登陆异常:{}", e.getMessage(), e);
        }
        return Result.failed("123");

    }

    /**
     * 登出 通用
     *
     * @param czToken
     * @return
     */
    @GetMapping("/loginOut")
    public Result<Boolean> loginOut(@Token CzToken czToken) {
        tokenService.destroy(czToken.getToken());
        return Result.succeed();
    }

    /**
     * 小程序用户信息绑定
     *
     * @param token
     * @param request
     * @return
     */

   /* @PostMapping("/maUserInfo")
    public Result<CzToken> maUserInfo(@Token CzToken token, @Valid @RequestBody BaseRequest<MaInfoQuery> request) {

        MaInfoQuery data = request.getData();
        data.setBizUserId(token.getBizUserId());

        return Result.succeed(czTokenService.maUserInfo(data));
    }*/


    /**
     * 小程序用户 绑定手机号
     *
     * @param token
     * @param request
     * @return
     */

    @PostMapping("/maBindPhone")
    public Result<CzToken> maBindPhone(@Token CzToken token, @Valid @RequestBody BaseRequest<MaPhoneQuery> request) {

        MaPhoneQuery data = request.getData();
        data.setBizUserId(token.getBizUserId());

        String sessionKey = data.getSessionKey();
        String encryptedData = data.getEncryptedData();
        String iv = data.getIv();
        String bizUserId = data.getBizUserId();

        // 解密
        WxMaPhoneNumberInfo phoneNoInfo = WxMaConfiguration.getMaService(appId).getUserService().getPhoneNoInfo(sessionKey, encryptedData, iv);

        TalentThird userThird = talentThirdMapper.selectOne(new LambdaQueryWrapper<>(TalentThird.class)
                .eq(TalentThird::getOpenId, data.getBizUserId()).eq(TalentThird::getAppId, appId));


        //此处为空 非法操作
        Assert.notNull(userThird);

        userThird.setPhone(phoneNoInfo.getPhoneNumber());
        UserDto userDto = new UserDto();

        //普通微信用户 更新手机号就好
        userThird.setUpdateTime(new Date());

        //根据手机号 获取下达人用户
        TalentUser dbUser = talentUserMapper.selectOne(new LambdaQueryWrapper<>(TalentUser.class)
                .eq(TalentUser::getPhone, userThird.getPhone()).last("limit 1"));
        Long talentUserId;
        if (dbUser == null) {
            //创建一个新的达人用户
            TalentUser talentUser = new TalentUser();
            talentUser.setPhone(userThird.getPhone());
            talentUser.setFirstTime(userThird.getCreateTime());
            talentUser.setRegisterTime(new Date());
            if (ObjectUtil.isNotNull(userThird.getAvatar()))
                talentUser.setAvatar(userThird.getAvatar());
            if (ObjectUtil.isNotNull(userThird.getNickName()))
                talentUser.setNickName(userThird.getNickName());
            talentUser.init();
            talentUserMapper.insert(talentUser);
            talentUserId = talentUser.getId();

        } else {
            talentUserMapper.updateById(dbUser);
            talentUserId = dbUser.getId();
        }

        userThird.setUserId(talentUserId);

        talentThirdMapper.update(userThird, new LambdaQueryWrapper<>(TalentThird.class)
                .eq(TalentThird::getAppId, appId).eq(TalentThird::getOpenId, bizUserId));

        userDto.setName(userThird.getNickName());
        userDto.setAvatar(userThird.getAvatar());
        userDto.setPhone(phoneNoInfo.getPhoneNumber());
        //后置头像 防止授权信息的时候 没有了
        userDto.setBizUserId(userThird.getOpenId());

        return Result.succeed(tokenService.createCzToken(userDto));
    }


    /**
     * 三方用户 查询或登陆
     *
     * @param newUser
     */
    private UserDto getOrCreateUser(TalentThird newUser) {
        UserDto userDto = new UserDto();

        TalentThird userThird = talentThirdMapper.selectOne(new LambdaQueryWrapper<>(TalentThird.class)
                .eq(TalentThird::getOpenId, newUser.getOpenId()).eq(TalentThird::getAppId, newUser.getAppId()));

        if (userThird == null) {
            newUser.init();
            talentThirdMapper.insert(newUser);
            userThird = newUser;
        } else if (ObjectUtil.isNotNull(userThird.getUserId())) {
            userDto.setId(userThird.getUserId());
        }

        //组装用户信息
        userDto.setAvatar(userThird.getAvatar());
        userDto.setName(userThird.getNickName());
        userDto.setPhone(userThird.getPhone());
        userDto.setBizUserId(userThird.getOpenId());

        return userDto;
    }


}
