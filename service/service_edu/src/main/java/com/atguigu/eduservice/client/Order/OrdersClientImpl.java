package com.atguigu.eduservice.client.Order;

import org.springframework.stereotype.Component;

@Component
public class OrdersClientImpl implements OrdersClient{
    @Override
    public boolean isBuyCourse(String courseId, String memberId) {
        return false;
    }
}
