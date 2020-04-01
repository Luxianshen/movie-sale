package com.github.lujs.community.controller;

import com.github.lujs.commmon.controller.BaseController;
import com.github.lujs.commmon.model.vo.BaseResponse;
import com.sun.imageio.plugins.common.ImageUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/community/upload")
public class UploadController extends BaseController {

    /**
     * 文件存储路径
     */
    @Value("${img.local.path}")
    private String imgLocalPath;
    /**
     * 文件网络访问路径
     */
    @Value("${img.host}")
    private String imgHost;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @PostMapping(value = "/image", produces = "application/json")
    public BaseResponse uploadImage(@RequestParam(required=true,value="file") MultipartFile file){
        if(null == file){
            return failedResponse("");
        }
        String random = RandomStringUtils.randomAlphabetic(16);
        String fileName = random + ".jpg";
        try {
            //todo 先本地储存
            String filePath = imgLocalPath+ File.separator + fileName;
            FileCopyUtils.copy(file.getBytes(), new File(imgLocalPath + File.separator, fileName));
            return successResponse(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return failedResponse("");
    }
}
