package com.atguigu.eduucenter.controller;

import com.atguigu.commonutils.JwtUtils;
import com.atguigu.eduucenter.entity.UcenterMember;
import com.atguigu.eduucenter.service.UcenterMemberService;
import com.atguigu.eduucenter.utils.ConstantWxUtils;
import com.atguigu.eduucenter.utils.HttpClientUtils;
import com.atguigu.servicebase.exceptionhandler.MyException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.sun.deploy.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@Controller
@RequestMapping("/api/ucenter/wx")
//@CrossOrigin
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;


    @GetMapping("callback")
    public String callback(String code, String state){
        try {
            //code: 临时票证,用来获取 access_token和open_id
            System.out.println("code:"+code);
            System.out.println("state:"+state);
            String baseAccessTokenUrl =
                    "https://api.weixin.qq.com/sns/oauth2/access_token" +
                            "?appid=%s" +
                            "&secret=%s" +
                            "&code=%s" +
                            "&grant_type=authorization_code";
            String accessTokenUrl = String.format(baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code);
            //使用httpclient发送请求
            String accessTokenInfo = HttpClientUtils.get(accessTokenUrl);
            System.out.println("accessTokenInfo:"+accessTokenInfo);
            Gson gson=new Gson();
            HashMap map = gson.fromJson(accessTokenInfo, HashMap.class);
            //得到access_token和openid
            String accessToken = (String) map.get("access_token");
            String openid = (String)map.get("openid");
            //发送请求获取扫描人的信息，如果没有登陆过则获取，登陆过则更新头像和昵称
            String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=%s" +
                    "&openid=%s";
            String userInfoUrl = String.format(baseUserInfoUrl, accessToken, openid);
            String userInfo = HttpClientUtils.get(userInfoUrl);
            System.out.println("userInfo:"+userInfo);
            HashMap userInfoMap = gson.fromJson(userInfo, HashMap.class);
            String nickName = (String) userInfoMap.get("nickname");
            String headimgurl = (String) userInfoMap.get("headimgurl");

            //添加扫描人的信息进入数据库
            //根据openid判断是否登陆过
            UcenterMember member = memberService.getOne(new QueryWrapper<UcenterMember>().eq("openid", openid));
            if(null==member){
                member=new UcenterMember();
                member.setOpenid(openid);
                member.setAvatar(headimgurl);
                member.setNickname(nickName);
                memberService.save(member);
            }
            //jwt生成token
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());
            //最后返回首页，并且通过路径传递token
            return "redirect:http://localhost:3000?token=" + jwtToken;
        } catch (Exception e){
            throw new MyException(20001,e.getMessage());
        }
    }

    @GetMapping("login")
    public String getWxCode(){
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对redirect_uri进行URL编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl= URLEncoder.encode(redirectUrl,"UTF-8");
        }catch (UnsupportedEncodingException e){
            throw new MyException(20001, e.getMessage());
        }

        String qrcodeUrl = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "atguigu");

        return "redirect:"+qrcodeUrl;
    }
}
