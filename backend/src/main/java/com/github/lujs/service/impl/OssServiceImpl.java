package com.github.lujs.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONUtil;
import com.github.lujs.service.IOssService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.AnonymousCOSCredentials;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class OssServiceImpl implements IOssService {

    private static final String VISIT_URL= "https://ms-1253382295.cos.ap-guangzhou.myqcloud.com/";

    private COSClient cosClient;

    @Value("${fileSavePath}")
    private String fileSavePath;

    @Value("${secretId}")
    private String secretId;

    @Value("${secretKey}")
    private String secretKey;

    /**
     * 初始化客户端
     */
    public COSClient getClient() {

        if (cosClient == null) {
            // 1 初始化用户身份信息（secretId, secretKey）。
            // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
            COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
            // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
            // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
            Region region = new Region("ap-guangzhou");
            ClientConfig clientConfig = new ClientConfig(region);
            // 这里建议设置使用 https 协议
            // 从 5.6.54 版本开始，默认使用了 https
            clientConfig.setHttpProtocol(HttpProtocol.https);
            // 3 生成 cos 客户端。
            cosClient = new COSClient(cred, clientConfig);
        }
        return cosClient;

    }

    @Override
    public String uploadQrcode(String qrcode) {

        // 指定文件将要存放的存储桶
        String bucketName = "ms-1253382295";

        String fileName = RandomUtil.randomString(8)+".jpg";
        File file = FileUtil.file(fileSavePath + fileName);
        QrCodeUtil.generate(qrcode, 300, 300, file);
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = "qrcode/" + fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        getClient().putObject(putObjectRequest);

        return VISIT_URL + key;
    }


    public static void main1(String[] args) {

        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        String secretId = "";//用户的 SecretId，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        String secretKey = "";//用户的 SecretKey，建议使用子账号密钥，授权遵循最小权限指引，降低使用风险。子账号密钥获取可参见 https://cloud.tencent.com/document/product/598/37140
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-guangzhou");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);

        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);

        String fileName = RandomUtil.randomString(10)+".jpg";
        File file = FileUtil.file("D:\\qrcode\\"+ fileName);
        QrCodeUtil.generate("2056454434345040", 300, 300, file);

        String bucketName = "ms-1253382295";
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = fileName;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        System.out.println(JSONUtil.toJsonStr(putObjectResult));

    }



}
