package org.apache.ydata;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubDataService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.Tools;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

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

    @Scheduled(cron = "0 0/30 * * * ?")
//    @Scheduled(cron = "0/60 * * * * ?")
    private void configureTasks() {
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

    public static <T> T post(String postUrl, Map<String, String> dataMap, Class<T> type) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        // 设置验签用的数据
        headers.add("Accept", "application/json,text/plain");
        // headers.add("Authorization", token);
        // 设置content-type,很据需求设置
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求体
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        if(!ObjectUtils.isEmpty(dataMap)) {
            dataMap.forEach((k, v) -> {
                map.add(k, dataMap.get(k));
            });
        }
        // 用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(postUrl, request, String.class);
        return JSONObject.parseObject(response.getBody(), type);
    }

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .dns(new xDns(15))
            .build();

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String get(String url, String token) throws IOException {
        Request.Builder builder = new Request.Builder()
                .url(url);
        if(!ObjectUtils.isEmpty(token)) {
            builder.addHeader("Authorization", token);
        }
        Request request = builder.get().build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public class xDns implements Dns {
        private long timeout  = 5;

        public xDns(long timeout){
            this.timeout = timeout;
        }

        @Override
        public List<InetAddress> lookup(String hostname) throws UnknownHostException {
            if(hostname == null){
                throw new UnknownHostException("host name is null");
            }
            else{
                try {
                    FutureTask<List<InetAddress>> task = new FutureTask<>(
                            new Callable<List<InetAddress>>() {
                                @Override
                                public List<InetAddress> call() throws Exception {
                                    return Arrays.asList(InetAddress.getAllByName(hostname));
                                }
                            });
                    new Thread(task).start();
                    return task.get(timeout, TimeUnit.SECONDS);
                } catch (Exception var4) {
                    UnknownHostException unknownHostException =
                            new UnknownHostException("Unable to resolve host " + hostname);
                    unknownHostException.initCause(var4);
                    throw unknownHostException;
                }
            }
        }
    }

}
