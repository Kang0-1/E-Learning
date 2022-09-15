package com.atguigu.eduorder.controller;


import com.atguigu.commonutils.JwtUtils;
import com.atguigu.commonutils.R;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2022-08-31
 */
@RestController
//@CrossOrigin
@RequestMapping("/eduorder/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 生成订单
     * @param courseId
     * @param request
     * @return
     */
    @PostMapping("createOrder/{courseId}")
    public R saveOrder(@PathVariable String courseId, HttpServletRequest request){
        //根据课程id和用户id
        //创建订单，返回订单号
        //从请求的header中的token获取用户信息
        String memberId = JwtUtils.getMemberIdByJwtToken(request);

        if(StringUtils.isEmpty(memberId)) {
            return R.error().code(28004).message("请登录");
        }
        String orderNo= orderService.createOrder(courseId, memberId);
        return R.ok().data("orderNo",orderNo);
    }

    //根据订单id查询订单信息
    @GetMapping("getOrderInfo/{orderId}")
    public R getOrderInfo(@PathVariable String orderId){
        Order order = orderService.getOne(new QueryWrapper<Order>().eq("order_no", orderId));
        return R.ok().data("order",order);
    }

}

