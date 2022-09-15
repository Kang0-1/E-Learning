package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduComment;

import com.atguigu.eduservice.mapper.EduCommentMapper;
import com.atguigu.eduservice.service.EduCommentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class EduCommentServiceImpl extends ServiceImpl<EduCommentMapper, EduComment> implements EduCommentService {

//    @Override
//    public List<EduComment>  getAllComment(Page<EduComment> page, String courseId) {
//        //所有评论
//        long current=page.getCurrent();
//        long size=page.getSize();
//        List<EduComment> list=baseMapper.selectList(new QueryWrapper<EduComment>().eq("course_id",courseId));
//
//        List<EduComment> commentList = list.stream().filter(eduComment -> eduComment.getParentCommentId().equals("0"))
//                .map(eduComment -> {
//                    eduComment.setCommentList(getChildren(eduComment, list));
//                    return eduComment;
//                })
//                .skip((current-1)*size).limit(size)
//                .sorted((e1, e2) -> e1.getGmtCreate().before(e2.getGmtCreate()) ? 1 : 0)
//                .collect(Collectors.toList());
//
//        return commentList;
//    }
//
//    private List<EduComment> getChildren(EduComment root, List<EduComment> all){
//        List<EduComment> children = all.stream().filter(eduSubject -> eduSubject.getParentCommentId().equals(root.getId()))
//                .map(eduComment -> {
//                    eduComment.setCommentList(getChildren(eduComment, all));
//                    return eduComment;
//                })
//                .sorted((e1,e2)-> e1.getGmtCreate().before(e2.getGmtCreate())?1:0)
//                .collect(Collectors.toList());
//        return  children;
//    }
}
