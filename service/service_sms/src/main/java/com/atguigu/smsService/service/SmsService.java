package com.atguigu.smsService.service;

public interface SmsService {
    boolean send(String email, String code);
}
