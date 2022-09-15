package com.atguigu.smsService.utils;

import org.apache.commons.mail.HtmlEmail;
import org.junit.Test;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


import java.util.Random;

public class EmailRegister {
    @Test
    public void test(){
        System.out.println(sendEmail("1215285086@qq.com",randomCode()));
    }

    //邮箱验证码
    public static boolean sendEmail(String emailAddress,String code){
        try {
            HtmlEmail email = new HtmlEmail();//不用更改
            email.setHostName("smtp.qq.com");//需要修改，126邮箱为smtp.126.com,163邮箱为163.smtp.com，QQ为smtp.qq.com
            email.setCharset("UTF-8");
            email.setSSLOnConnect(false); //重要设置
            email.addTo(emailAddress);// 收件地址
            email.setFrom("1215285086@qq.com", "Administrator");//此处填邮箱地址和用户名,用户名可以任意填写
            email.setAuthentication("1215285086@qq.com", "ytrvyukwrnkifdii");//此处填写邮箱地址和客户端授权码
            email.setSubject("e-Learning-Online");//此处填写邮件名，邮件名可任意填写
            email.setMsg("尊敬的用户您好,您本次注册的验证码是:" + code+"，2分钟内有效！");//此处填写邮件内容

            email.send();
            return true;
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public void sendSimpleMail() throws Exception {
        JavaMailSender javaMailSender=new JavaMailSenderImpl();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("test");
        message.setTo("1215285086@qq.com");
        message.setSubject("主题：简单邮件");
        message.setText("测试邮件内容");
        javaMailSender.send(message);
    }
    public static String randomCode(){
        StringBuilder stringBuilder=new StringBuilder();
        Random random=new Random();
        //int len=random.nextInt(10);
        for (int i = 0; i < 6; i++) {
            stringBuilder.append(random.nextInt(10));
        }
        return stringBuilder.toString();
    }
}
