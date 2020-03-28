package com.github.lujs.commmon.controller;

import com.github.lujs.commmon.exception.status.GlobalStatusCode;
import com.github.lujs.commmon.model.vo.BaseResponse;

/**
 * 基础控制器
 * @author binchao
 * @date: 2019-01-29 18:19
 * @desc:
 */
public class BaseController {

    /**
     *  标准返回
     * @param result
     * @return
     */
    protected BaseResponse baseResponse(boolean result) {
        if (result) {
            return new BaseResponse();
        } else {
            return new BaseResponse(GlobalStatusCode.FAILED);
        }
    }

    /**
     * 标准成功状态返回并附带数据
     * @param data
     * @return
     */
    protected BaseResponse successResponse(Object data) {
        BaseResponse response = new BaseResponse();
        response.setData(data);
        return response;
    }

    /**
     * 标准失败状态返回并附带数据
     * @param data
     * @return
     */
    protected BaseResponse failedResponse(Object data) {
        BaseResponse response = new BaseResponse();
        response.setFailed();
        response.setData(data);
        return response;
    }

}
