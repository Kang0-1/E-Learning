package com.atguigu.staservice.schedule;

import com.atguigu.staservice.service.StatisticsDailyService;
import com.atguigu.staservice.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.atguigu.staservice.utils.DateUtil.addDays;

@Component
//定时任务
public class ScheduleTask {

    @Autowired
    private StatisticsDailyService staService;
    @Scheduled(cron = "0 0 1 * * ?")
    public void task1(){
        staService.registerCount(DateUtil.formatDate(addDays(new Date(),-1)));
    }
}
