package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.OssService;
import com.atguigu.oss.utils.ConstantPropertiesUtils;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class OssServiceImpl implements OssService {

    //阿里云做存储空间
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        String endpoint = ConstantPropertiesUtils.END_POINT;
        String accessKeyId = ConstantPropertiesUtils.ACCESS_KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.ACCESS_KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET_NAME;
        //修改文件在空间保存的名称
        // 获取文件名称
        String filename =file.getOriginalFilename();
        filename=FileNameChange(filename);

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = file.getInputStream();
            ossClient.putObject(bucketName, filename, inputStream);
            //https://edu-513.oss-cn-chengdu.aliyuncs.com/01.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+filename;
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }  finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    //七牛云做存储空间
    @Override
    public String uploadFileAvatarByQiNiu(MultipartFile file) {
        String access_key=ConstantPropertiesUtils.ACCESS_KEY;
        String secret_key=ConstantPropertiesUtils.SECRET_KEY;
        String buketName=ConstantPropertiesUtils.qiNiuBucketName;

        //URLDecoder.decode(ss,"UTF-8");//后台接到时候进行转码

        //密钥配置
        Auth auth=Auth.create(access_key,secret_key);
        String token=auth.uploadToken(buketName);
        //创建上传对象
        UploadManager uploadManager=new UploadManager(new Configuration());
        //修改文件在空间保存的名称

        String filename=file.getOriginalFilename();
        if(checkChinese(filename)){
            filename=".jpg";
        }
        filename=FileNameChange(filename);
        try {
            Response response=uploadManager.put(file.getBytes(),filename,token);
            System.out.println(response.bodyString());
            return filename;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String FileNameChange(String filename){
        //加上UUID随机值
        String uuid= UUID.randomUUID().toString().replaceAll("-","");
        filename=uuid+"-"+filename;
        //把文件按照日期分类
        String datePath= new DateTime().toString("yyyy/MM/dd");
        filename=datePath+"/"+filename;
        return filename;
    }
    public boolean checkChinese(String str)
    {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
