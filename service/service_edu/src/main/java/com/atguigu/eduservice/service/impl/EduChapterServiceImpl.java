package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService videoService;//注入小节service

    //课程大纲列表,根据课程id进行查询
    @Override
    public List<EduChapter> getChapterVideoByCourseId(String courseId) {
        //根据课程id查出该课程的所有章节
        //1.查出所有章节
        List<EduChapter> list=baseMapper.selectList(null);
        List<EduVideo> videoList=videoService.list(null);
        //2.根据课程id选出该课程所有章节
        List<EduChapter> eduChapterList=list.stream().filter(eduChapter -> eduChapter.getCourseId().equals(courseId))
                .map(eduChapter -> {
                    eduChapter.setEduVideoList(getChildren(eduChapter,videoList));
                    return eduChapter;
                })
                .sorted(Comparator.comparingInt(EduChapter::getSort))
                .collect(Collectors.toList());

        return eduChapterList;

    }
    private List<EduVideo> getChildren(EduChapter root, List<EduVideo> all){
        List<EduVideo> children = all.stream().filter(eduVideo -> eduVideo.getChapterId().equals(root.getId()))
                .sorted(Comparator.comparingInt(EduVideo::getSort))
                .collect(Collectors.toList());
        return  children;
    }

    ////删除章节的方法
    @Override
    public boolean deleteChapter(String chapterId) {
        int count = videoService.count(new QueryWrapper<EduVideo>().eq("chapter_id", chapterId));
        if(count>0){
            //查询出小节 不删除章节
            throw new MyException(20001,"此章节下有小节，请先删除小节");
        }else {
            //该章节下无小节
            int i = baseMapper.deleteById(chapterId);
            return i>0;
        }
    }

    //2 根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
