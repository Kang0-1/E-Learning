package com.atguigu.eduucenter.service;

import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.entity.Vo.Register;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author testjava
 * @since 2022-08-26
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember member);

    boolean register(Register register);

    Integer countRegister(String day);
}
