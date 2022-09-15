package com.atguigu.eduorder.service.impl;

import com.atguigu.commonutils.orderVo.CourseWebVoOrder;
import com.atguigu.commonutils.orderVo.UcenterMemberOrder;
import com.atguigu.eduorder.client.EduClient;
import com.atguigu.eduorder.client.UcenterClient;
import com.atguigu.eduorder.entity.Order;
import com.atguigu.eduorder.mapper.OrderMapper;
import com.atguigu.eduorder.service.OrderService;
import com.atguigu.eduorder.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2022-08-31
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private EduClient eduClient;

    @Autowired
    private UcenterClient ucenterClient;

    @Override
    public String createOrder(String courseId, String memberId) {
        //通过远程调用获得课程信息和用户信息
        UcenterMemberOrder userInfo = ucenterClient.getInfo(memberId);
        CourseWebVoOrder courseInfo = eduClient.getCourseInfoOrder(courseId);
        Order order=new Order();
        order.setOrderNo(OrderNoUtil.getOrderNo());
        order.setCourseId(courseId);
        order.setCourseTitle(courseInfo.getTitle());
        order.setCourseCover(courseInfo.getCover());
        order.setTeacherName("test");
        order.setTotalFee(courseInfo.getPrice());

        order.setMemberId(memberId);
        order.setEmail(userInfo.getEmail());
        order.setNickname(userInfo.getNickname());

        order.setStatus(0); // 订单状态(0:未支付)
        order.setPayType(1); //支付类型(1:微信)
        baseMapper.insert(order);

        return order.getOrderNo();
    }
}
