package com.atguigu.eduservice.controller.front;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@CrossOrigin
@RestController
@RequestMapping("/eduservice/teacherfront")
public class TeacherFrontController {

    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //讲师分页查询
    @GetMapping("getTeacherFrontList/{current}/{limit}")
    public R getTeacherFrontList(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> page=new Page<>(current,limit);
        Map<String,Object> map=teacherService.getTeacherFrontList(page);
        return R.ok().data(map);
    }

    //讲师详情
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){
        EduTeacher teacher = teacherService.getById(teacherId);
        List<EduCourse> courseList = courseService.list(new QueryWrapper<EduCourse>().eq("teacher_id", teacherId));
        return R.ok().data("teacher",teacher).data("courseList",courseList);
    }
}
