package com.atguigu.smsService.service.impl;

import com.atguigu.smsService.service.SmsService;
import com.atguigu.smsService.utils.EmailRegister;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {
    @Override
    public boolean send(String email, String code) {
        return EmailRegister.sendEmail(email,code);
    }
}
