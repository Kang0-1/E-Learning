package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.Vod.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //注入vodClient
    @Autowired
    private VodClient vodClient;

    //1 根据课程id删除小节
    @Override
    public void removeVideoByCourseId(String courseId) {
        //1 根据课程id查询课程所有的视频id
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        wrapperVideo.eq("course_id",courseId);
        wrapperVideo.select("video_source_id");
        List<EduVideo> eduVideoList = baseMapper.selectList(wrapperVideo);

//        // List<EduVideo>变成List<String>
//        List<String> videoIds = new ArrayList<>();
//        for (int i = 0; i < eduVideoList.size(); i++) {
//            EduVideo eduVideo = eduVideoList.get(i);
//            String videoSourceId = eduVideo.getVideoSourceId();
//            if(!StringUtils.isEmpty(videoSourceId)) {
//                //放到videoIds集合里面
//                videoIds.add(videoSourceId);
//            }
//        }
        List<String> videoIds = eduVideoList.stream().
                map(EduVideo::getVideoSourceId).
                filter(Objects::nonNull).
                collect(Collectors.toList());
        //根据多个视频id删除多个视频
        if(videoIds.size()>0) {
            vodClient.deleteBatch(videoIds);
        }

        //先删视频 再删小节信息
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
