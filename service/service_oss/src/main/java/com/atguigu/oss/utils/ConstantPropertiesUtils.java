package com.atguigu.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//当项目已启动，spring接口，spring加载之后，执行接口一个方法
@Component
public class ConstantPropertiesUtils implements InitializingBean {

    //读取配置文件内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

//    @Value("${aliyun.oss.file.filehost}")
//    private String fileHost;

    @Value("${aliyun.oss.file.bucketname}")
    private String bucketName;

    @Value("${qiniu.ACCESS_KEY}")
    private String AK;

    @Value("${qiniu.SECRET_KEY}")
    private String SK;

    @Value("${qiniu.bucketname}")
    private String qiniuBucketName;

    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;
    //public static String FILE_HOST ;

    public static String ACCESS_KEY;
    public static String SECRET_KEY;
    public static String qiNiuBucketName;

    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT = endpoint;
        ACCESS_KEY_ID = keyId;
        ACCESS_KEY_SECRET = keySecret;
        BUCKET_NAME = bucketName;
        //FILE_HOST = fileHost;

        ACCESS_KEY=AK;
        SECRET_KEY=SK;
        qiNiuBucketName=qiniuBucketName;

    }
}
