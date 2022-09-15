package com.atguigu.eduservice.client.Ucenter;

import com.atguigu.commonutils.orderVo.UcenterMemberOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "service-ucenter",fallback = UcentClientImpl.class)
@Component
public interface UcenterClient {

    //根据用户id获取用户信息
    @PostMapping("/eduucenter/member/getInfoUc/{id}")
    public UcenterMemberOrder getInfo(@PathVariable("id") String id);
}
