package com.atguigu.eduservice.controller.front;

import org.springframework.util.StringUtils;
import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.orderVo.CourseWebVoOrder;
import com.atguigu.eduservice.client.Order.OrdersClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.frontVo.CourseFrontVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

//@CrossOrigin
@RestController
@RequestMapping("/eduservice/coursefront")
public class CourseFrontController {

    @Autowired
    private OrdersClient ordersClient;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduCourseService courseService;

    //课程分页
    @PostMapping("getFrontCourseList/{current}/{limit}")
    public R getFrontCourseList(@PathVariable long current, @PathVariable long limit,
                                @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> page=new Page<>(current,limit);
        Map<String,Object> map=courseService.getFrontCourseList(page,courseFrontVo);
        return R.ok().data(map);
    }

    //课程详情
    @GetMapping("getFrontCourseInfo/{courseId}")
    public R getFrontCourseInfo(@PathVariable String courseId, HttpServletRequest request){
        //1.根据课程id查询课程信息   返回vo
        CourseWebVo courseWebVo= courseService.getBaseCourseInfo(courseId); //SQL语句
        //2.根据课程id查询章节和小节
        List<EduChapter> chapterVideoList = chapterService.getChapterVideoByCourseId(courseId);

        //远程调用order中的接口-查询已登陆用户是否购买该课程
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        if(StringUtils.isEmpty(memberId)){
            //return R.error().code(28004).message("请先登陆");
            return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",false).message("未登录");
        }
        boolean buyCourse = ordersClient.isBuyCourse(courseId, memberId);
        return R.ok().data("courseWebVo",courseWebVo).data("chapterVideoList",chapterVideoList).data("isBuy",buyCourse);

    }

    //根据课程id查询课程信息
    @PostMapping("/getCourseInfoOrder/{id}")
    public CourseWebVoOrder getCourseInfoOrder(@PathVariable String id){
        CourseWebVo courseInfo = courseService.getBaseCourseInfo(id);
        CourseWebVoOrder courseWebVoOrder=new CourseWebVoOrder();
        BeanUtils.copyProperties(courseInfo,courseWebVoOrder);
        return courseWebVoOrder;
    }
}
