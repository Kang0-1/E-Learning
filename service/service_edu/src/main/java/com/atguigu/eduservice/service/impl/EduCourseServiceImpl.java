package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.frontVo.CourseFrontVo;
import com.atguigu.eduservice.entity.vo.CourseInfoVo;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQuery;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.atguigu.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-03-02
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {


    //注入小节和章节service
    @Autowired
    private EduVideoService videoService;

    @Autowired
    private EduChapterService chapterService;

    //添加课程基本信息的方法
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //向课程表添加课程基本信息
        EduCourse eduCourse = new EduCourse();
        BeanUtils.copyProperties(courseInfoVo, eduCourse);
        int insert = baseMapper.insert(eduCourse);
        if (insert <= 0) {
            throw new MyException(20001, "添加课程失败");
        }
        return eduCourse.getId();
    }

    @Override
    public void pageQuery(Page<EduCourse> page, CourseQuery courseQuery) {
        QueryWrapper<EduCourse> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if(courseQuery==null){
            baseMapper.selectPage(page,queryWrapper);
            return;
        }
        String title=courseQuery.getTitle();
        String teacherId = courseQuery.getTeacherId();
        String subjectParentId = courseQuery.getSubjectParentId();
        String subjectId = courseQuery.getSubjectId();

        queryWrapper.like(!StringUtils.isEmpty(title),"title",title);
        queryWrapper.eq(!StringUtils.isEmpty(teacherId),"teacher_id",teacherId);
        queryWrapper.eq(!StringUtils.isEmpty(subjectParentId),"subject_parent_id",subjectParentId);
        queryWrapper.eq(!StringUtils.isEmpty(subjectId),"subject_id",subjectId);
        baseMapper.selectPage(page,queryWrapper);
    }

    @Cacheable(key = "'getIndexCourse'",value = "course")
    @Override
    public List<EduCourse> getIndexCourse() {
        QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("view_count");
        wrapper.last("limit 8");
        List<EduCourse> courses = baseMapper.selectList(wrapper);
        return courses;
    }

    //条件查询带分页 （前台系统展示）
    @Override
    public Map<String, Object> getFrontCourseList(Page<EduCourse> page, CourseFrontVo courseFrontVo) {
        QueryWrapper<EduCourse> queryWrapper=new QueryWrapper<>();
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())){//一级分类
            queryWrapper.eq("subject_parent_id",courseFrontVo.getSubjectParentId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())){//二级分类
            queryWrapper.eq("subject_id",courseFrontVo.getSubjectId());
        }
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){//销量排序
            queryWrapper.orderByDesc("buy_count");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){//价格排序
            queryWrapper.orderByDesc("price");
        }
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){//最新时间排序
            queryWrapper.orderByDesc("gmt_create");
        }
        baseMapper.selectPage(page,queryWrapper);
        Map<String,Object> map=new HashMap<>();
        map.put("items", page.getRecords());
        map.put("current", page.getCurrent());
        map.put("pages", page.getPages());
        map.put("size", page.getSize());
        map.put("total", page.getTotal());
        map.put("hasNext", page.hasNext());
        map.put("hasPrevious", page.hasPrevious());
        return map;
    }

    @Override
    public CourseWebVo getBaseCourseInfo(String courseId) {
        return baseMapper.getBaseCourseInfo(courseId);
    }


    //根据课程id查询课程确认信息
    @Override
    public CoursePublishVo publishCourseInfo(String id) {
        //调用mapper
        CoursePublishVo coursePublishVo = baseMapper.getCoursePublishVo(id);
        return coursePublishVo;
    }

    //删除课程

    @Override
    public void removeCourse(String courseId) {
        //1.根据id删除小节
        videoService.removeVideoByCourseId(courseId);
        //2.根据id删除章节
        chapterService.remove(new QueryWrapper<EduChapter>().eq("course_id",courseId));
        //3.根据id删除课程本身
        baseMapper.deleteById(courseId);
    }

}
