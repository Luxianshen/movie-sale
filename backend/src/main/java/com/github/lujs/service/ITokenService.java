package com.github.lujs.service;

import com.github.lujs.commmon.CzToken;
import com.github.lujs.model.UserDto;
import com.github.lujs.model.pojo.WxLocation;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/13 20:39
 */
public interface ITokenService {

    CzToken createToken(UserDto userDto, WxLocation location);

    CzToken getToken(String key);

    void refresh(String key);

    void destroy(String token);

}
