package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.service.PayLogService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-31
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduorder/paylog")
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    @Autowired
    private OrderService orderService;

    //生成微信支付二维码接口
    @GetMapping("createNative/{orderNo}")
    public R createNative(@PathVariable String orderNo){
        Map map= payLogService.createNative(orderNo);
        System.out.println("生成微信支付二维码map:"+map);
        return R.ok().data(map);
    }

    //查询订单支付状态
    @GetMapping("queryPayStatus/{orderNo}")
    public R queryPayStatus(@PathVariable String orderNo){
        Map<String ,String> map= payLogService.queryPayStatus(orderNo);
        System.out.println("查询订单状态map:"+map);
        if(map==null){
            return R.error().message("支付出错了");
        }
        //map不为空，获取map中的订单状态
        if(map.get("trade_state").equals("SUCCESS")){
            //添加记录到支付表，并更新订单表支付状态
            payLogService.updateOrderStatus(map);
            return R.ok().message("支付成功!");
        }
        return R.ok().code(25000).message("支付中");
    }

    //根据课程id和用户id查询订单表中订单状态
    @GetMapping("isBuyCourse/{courseId}/{memberId}")
    public boolean isBuyCourse(@PathVariable String courseId,@PathVariable String memberId){
        int count = orderService.count(new QueryWrapper<Order>().eq("course_id", courseId).eq("member_id", memberId).eq("status", 1));
        if(count>0){
            return true;
        }else {
            return false;
        }

    }

}

