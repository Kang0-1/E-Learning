package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.excel.SubjectData;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-02-29
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    //添加课程分类
    @Override
    public void saveSubject(MultipartFile file,EduSubjectService subjectService) {
        try {
            //文件输入流
            InputStream in = file.getInputStream();
            //调用方法进行读取
            EasyExcel.read(in, SubjectData.class,new SubjectExcelListener(subjectService)).sheet().doRead();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //课程分类列表（树形）
    @Override
    public List<EduSubject> getAllSubject() {
        //查出所有分类
        List<EduSubject> list = baseMapper.selectList(null);

        //找出一级分类
        List<EduSubject> OneSubject = list.stream().filter(eduSubject -> eduSubject.getParentId().equals("0"))
                .map(eduSubject ->{
                    eduSubject.setChildren(getChildren(eduSubject,list));
                    return eduSubject;
                } )
                .sorted((e1,e2)-> e1.getSort()- e2.getSort())
                .collect(Collectors.toList());
        return OneSubject;
    }

    private List<EduSubject> getChildren(EduSubject root,List<EduSubject> all){
        List<EduSubject> children = all.stream().filter(eduSubject -> eduSubject.getParentId().equals(root.getId()))
                .sorted((e1,e2)-> e1.getSort()- e2.getSort())
                //因为这里只需要二级分类 所有不用再找子分类
                .collect(Collectors.toList());
        return  children;
    }


}
