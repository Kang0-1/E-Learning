package com.atguigu.eduservice.client.Vod;

import com.atguigu.commonutils.R;
import com.atguigu.eduservice.client.Vod.VodClient;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
//调用服务出错时执行下面的方法
public class VodFileDegradeFeignClient  implements VodClient {
    @Override
    public R removeAlyVideo(String id) {
        return R.error().message("删除视频出错了");
    }

    @Override
    public R deleteBatch(List<String> videoIdList) {
        return R.error().message("删除多个视频出错了");
    }
}
