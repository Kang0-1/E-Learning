package com.atguigu.smsService.controller;

import com.atguigu.commonutils.R;
import com.atguigu.smsService.service.SmsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.concurrent.TimeUnit;

import static com.atguigu.smsService.utils.EmailRegister.randomCode;

@RestController
@RequestMapping("/edusms")
public class SmsController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String ,String> redisTemplate;

    @GetMapping("send/{email}")
    public R sendSms(@PathVariable String email){
        //从redis获取验证码，如果在2分钟内存在该邮箱的注册则直接返回
        String s = redisTemplate.opsForValue().get(email);
        if(!StringUtils.isEmpty(s)){
            return R.ok();
        }
        String code=randomCode();
        boolean isSend=smsService.send(email,code);
        if(isSend){
            //设置验证码有效期为2分钟
            redisTemplate.opsForValue().set(email,code,2, TimeUnit.MINUTES);
            return R.ok();
        }else {
            return R.error().message("验证码发送失败");
        }

    }

}
