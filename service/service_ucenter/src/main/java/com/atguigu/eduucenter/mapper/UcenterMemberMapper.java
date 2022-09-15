package com.atguigu.eduucenter.mapper;

import com.atguigu.eduucenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author testjava
 * @since 2022-08-26
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    int countRegister(String day);
}
