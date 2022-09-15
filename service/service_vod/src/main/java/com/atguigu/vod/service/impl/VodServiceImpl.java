package com.atguigu.vod.service.impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.atguigu.servicebase.exceptionhandler.MyException;
import com.atguigu.vod.Utils.ConstantVodUtils;
import com.atguigu.vod.Utils.initVodClient;
import com.atguigu.vod.service.VodService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class VodServiceImpl implements VodService {

    @Override
    public String uploadVideoAly(MultipartFile file) throws IOException {
        String filename= file.getOriginalFilename();
        String title=filename.substring(0,filename.lastIndexOf("."));//截取名字不包含 "mp4";
        InputStream inputStream= file.getInputStream();
        UploadStreamRequest request=new UploadStreamRequest(ConstantVodUtils.ACCESS_KEY_ID,ConstantVodUtils.ACCESS_KEY_SECRET,title,filename,inputStream);
        UploadVideoImpl uploader=new UploadVideoImpl();
        UploadStreamResponse response=uploader.uploadStream(request);
        String videoId;
        if(response.isSuccess()){
            videoId=response.getVideoId();
        }else {
            videoId=response.getVideoId();
        }
        return videoId;

    }

    @Override
    public void removeVideo(String id) {
        try{
            //初始化对象
            DefaultAcsClient client = initVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频
            DeleteVideoRequest request=new DeleteVideoRequest();
            request.setVideoIds(id);
            client.getAcsResponse(request);
        }catch (Exception e){
            throw new MyException(20001,"删除视频失败");
        }
    }

    @Override
    public void removeMoreAlyVideo(List videoIdList) {
        try {
            //初始化对象
            DefaultAcsClient client = initVodClient.initVodClient(ConstantVodUtils.ACCESS_KEY_ID, ConstantVodUtils.ACCESS_KEY_SECRET);
            //创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //videoIdList值转换成 1,2,3
            String videoIds = StringUtils.join(videoIdList.toArray(), ",");

            //向request设置视频id
            request.setVideoIds(videoIds);
            //调用初始化对象的方法实现删除
            client.getAcsResponse(request);
        }catch(Exception e) {
            e.printStackTrace();
            throw new MyException(20001,"删除视频失败");
        }
    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("11");
        list.add("22");
        list.add("33");
        // 11,22,33
        String join = StringUtils.join(list.toArray(), ",");
        String join1=String.join(",",list);
        System.out.println(join1);
    }
}
