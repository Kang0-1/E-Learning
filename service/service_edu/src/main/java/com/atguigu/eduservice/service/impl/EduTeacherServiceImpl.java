package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.mapper.EduTeacherMapper;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-02-24
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    @Override
    public void pageQuery(Page<EduTeacher> page, TeacherQuery teacherQuery) {
        QueryWrapper<EduTeacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        if(teacherQuery==null){
            baseMapper.selectPage(page,queryWrapper);
            return;
        }
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        queryWrapper.like(!StringUtils.isEmpty(name),"name",name);
        queryWrapper.eq(!StringUtils.isEmpty(level),"level", level);
        queryWrapper.ge(!StringUtils.isEmpty(begin),"gmt_create", begin);//大于等于(ge)开始时间
        queryWrapper.le(!StringUtils.isEmpty(end),"gmt_create", end);//小于等于(le)结束时间
        baseMapper.selectPage(page,queryWrapper);
    }

    @Cacheable(key = "'getIndexTeacher'",value = "teacher")
    @Override
    public List<EduTeacher> getIndexTeacher() {
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<>();
        wrapper.orderByDesc("id");
        wrapper.last("limit 4");
        List<EduTeacher> eduTeachers = baseMapper.selectList(wrapper);
        return eduTeachers;
    }

    @Override
    public Map<String, Object> getTeacherFrontList(Page<EduTeacher> page) {
        baseMapper.selectPage(page,new QueryWrapper<EduTeacher>().orderByDesc("id"));
        //获取分页数据放到map中
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
}
