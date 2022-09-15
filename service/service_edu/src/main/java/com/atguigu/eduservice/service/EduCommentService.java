package com.atguigu.eduservice.service;

import com.atguigu.eduservice.entity.EduComment;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface EduCommentService extends IService<EduComment> {
    //List<EduComment>  getAllComment(Page<EduComment> page, String courseId);
}
