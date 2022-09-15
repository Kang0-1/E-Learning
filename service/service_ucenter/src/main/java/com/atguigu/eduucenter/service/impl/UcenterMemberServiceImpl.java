package com.atguigu.eduucenter.service.impl;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.MD5;
import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.entity.Vo.Register;
import com.atguigu.eduucenter.mapper.UcenterMemberMapper;
import com.atguigu.eduucenter.service.UcenterMemberService;
import com.atguigu.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-26
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    private RedisTemplate<String ,String > redisTemplate;

    @Override
    public String login(UcenterMember member) {
        String email=member.getEmail();
        String password=member.getPassword();
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)){
            throw new MyException(20001,"登陆失败");
        }
        UcenterMember emailMember = baseMapper.selectOne(new QueryWrapper<UcenterMember>().eq("email", email));
        if(null==emailMember){
            throw new MyException(20001,"登陆失败,查无此邮箱");
        }
        //判断密码
        //把输入密码加密后与数据库密码比对
        //数据库存储密码已MD5加密
        if(!MD5.encrypt(password).equals(emailMember.getPassword())){
            throw new MyException(20001,"登陆失败,请验证密码");
        }
        //判断用户是否禁用
        if(emailMember.getIsDisabled()){
            throw new MyException(20001,"登陆失败,该用户已被禁用");
        }
        //验证成功 可以登陆
        //返回token
        String jwtToken = JwtUtils.getJwtToken(emailMember.getId(), emailMember.getNickname());
        return jwtToken;
    }

    @Override
    public boolean register(Register register) {
        String code=register.getCode();
        String email = register.getEmail();
        String nickname = register.getNickname();
        String password = register.getPassword();
        //判断为空
        if(StringUtils.isEmpty(email)||StringUtils.isEmpty(password)||StringUtils.isEmpty(code)||StringUtils.isEmpty(nickname)){
            throw new MyException(20001,"注册失败");
        }
        //判断验证码是否一致
        //从redis中获取对比
        String redisCode = redisTemplate.opsForValue().get(email);
        if(redisCode==null){throw new MyException(20001,"验证码失效");}
        if(!code.equals(redisCode)){
            throw new MyException(20001,"注册失败");
        }
        //判断邮箱是否已经注册
        Integer count = baseMapper.selectCount(new QueryWrapper<UcenterMember>().eq("email", register.getEmail()));
        if(count>0){throw new MyException(20001,"注册失败,邮箱已注册");}
        UcenterMember member=new UcenterMember();
        member.setEmail(email);member.setNickname(nickname);member.setPassword(MD5.encrypt(password));member.setIsDisabled(false);
        member.setAvatar("https://edu-513.oss-cn-chengdu.aliyuncs.com/2022/08/21/ba12fc0c443542469f4f188fdf65eb5bfile.png");
        int i = baseMapper.insert(member);
        return i == 1;
    }

    @Override
    public Integer countRegister(String day) {

        return baseMapper.countRegister(day);
    }
}
