package org.apache.ydata;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubDataService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.Tools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
public class ScheduleTask {

    private static JSONObject tokenResult;
    private static long getTokenTime = 0L;

    @Resource
    private HubInfoService hubInfoService;

    @Resource
    private HubDataService hubDataService;

    @Resource
    private ZyAdapter zyAdapter;

    @Value("${config.isPub}")
    private boolean isPub;

    @Scheduled(cron = "0 0/30 * * * ?")
//    @Scheduled(cron = "0/60 * * * * ?")
    private void configureTasks() {
        if(!isPub) return;
        log.info("执行数据查询时间: " + LocalDateTime.now());
        List<HubInfo> hubInfoList = hubInfoService.selectAll();
        if(!ObjectUtils.isEmpty(hubInfoList)) {
            long statTime = System.currentTimeMillis() - 1000;
            hubInfoList.forEach(item -> {
                try {
                    JSONObject jsonObject = zyAdapter.stat(item, Tools.timeDateString(statTime, "yyyy-MM-dd"));
                    //解析并写入库
                    hubDataService.insertOrUpdate(item, jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
