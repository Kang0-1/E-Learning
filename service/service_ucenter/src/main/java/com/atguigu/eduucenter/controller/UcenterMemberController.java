package com.atguigu.eduucenter.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.commonutils.orderVo.UcenterMemberOrder;
import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.entity.Vo.Register;
import com.atguigu.eduucenter.service.UcenterMemberService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-26
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduucenter/member")
public class UcenterMemberController {

    @Autowired
    private UcenterMemberService memberService;

    //登陆
    @PostMapping("/login")
    public R loginUser(@RequestBody UcenterMember member){
        //返回token值，使用jwt生成
        String token= memberService.login(member);
        return R.ok().data("token",token);
    }

    //注册
    @PostMapping("register")
    public R registerUser(@RequestBody Register register){
        boolean b = memberService.register(register);
        return b?R.ok():R.error();
    }


    //根据token获取用户信息
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request) {
        //调用jwt工具类，根据request对象获取头信息，返回用户id
        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //查询数据库根据id获取用户信息
        UcenterMember member = memberService.getById(memberId);
        return R.ok().data("userInfo",member);
    }

    //根据用户id获取用户信息
    @PostMapping("getInfoUc/{id}")
    public UcenterMemberOrder getInfo(@PathVariable String id) {
        UcenterMember member = memberService.getById(id);
        UcenterMemberOrder ucenterMemberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,ucenterMemberOrder);
        return ucenterMemberOrder;
    }

    //统计模块调用接口
    //查询某一天的注册人数
    @GetMapping("countRegister/{day}")
    public R countRegister(@PathVariable String day){
        int count=memberService.countRegister(day);
        return R.ok().data("countRegister",count);
    }

}

