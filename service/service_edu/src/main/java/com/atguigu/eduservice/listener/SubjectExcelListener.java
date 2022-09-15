package com.atguigu.eduservice.listener;

import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.context.AnalysisContext;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


public class SubjectExcelListener extends AnalysisEventListener<SubjectData> {

    //因为SubjectExcelListener不能交给spring进行管理 需要自己new
    public EduSubjectService eduSubjectService;

    public SubjectExcelListener() {}
    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }

    //读取excel表格，一行一行进行读取
    @Override
    public void invoke(SubjectData subjectData,AnalysisContext analysisContext){
       if(subjectData==null){
           throw new MyException(20001,"文件数据为空");
       }

       //一行一行读取，每次读取有两个值，第一个值是一级分类，第二个值是二级分类

       //判断一级分类是否重复
        EduSubject existOneSubject = this.existOneSubject(eduSubjectService, subjectData.getOneSubjectName());
       if(existOneSubject==null){  //没有相同一级分类,进行添加
           existOneSubject=new EduSubject();
           existOneSubject.setParentId("0");
           existOneSubject.setTitle(subjectData.getOneSubjectName());
           eduSubjectService.save(existOneSubject);
       }

        //判断二级分类是否重复
        String pid=existOneSubject.getId();//获取一级分类的id值

        EduSubject existTwoSubject = this.existTwoSubject(eduSubjectService, subjectData.getTwoSubjectName(), pid);
        if(existTwoSubject==null){  //没有相同二级分类,进行添加
            existTwoSubject=new EduSubject();
            existTwoSubject.setParentId(pid);
            existTwoSubject.setTitle(subjectData.getTwoSubjectName());
            eduSubjectService.save(existTwoSubject);
        }


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext){

    }

    //判断一级分类不能重复添加
    private EduSubject existOneSubject(EduSubjectService subjectService,String name){
        return subjectService.getOne(new QueryWrapper<EduSubject>().eq("title", name).eq("parent_id", "0"));
    }

    //判断二级分类不能重复添加
    private EduSubject existTwoSubject(EduSubjectService subjectService,String name,String pid){
        return subjectService.getOne(new QueryWrapper<EduSubject>().eq("title", name).eq("parent_id", pid));
    }

}
