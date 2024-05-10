package org.apache.ydata.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

@Slf4j
public class HttpUtils {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .dns(new xDns(5))
            .build();

    public static JSONObject post(String postUrl, Map<String, String> headerMap, Map<String, String> dataMap) {
        log.info("Post={}", postUrl);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        // 设置验签用的数据
        if(!ObjectUtils.isEmpty(headerMap)) {
            headerMap.forEach((k, v) -> {
                headers.add(k, headerMap.get(k));
            });
        }
        // 设置content-type,很据需求设置
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        // 设置请求体
        MultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        if(!ObjectUtils.isEmpty(dataMap)) {
            dataMap.forEach((k, v) -> {
                bodyMap.add(k, dataMap.get(k));
            });
        }
        // 用HttpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(bodyMap, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(postUrl, request, String.class);
        String content = response.getBody();
        log.info("Result={}", content);
        return JSONObject.parseObject(content);
    }

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
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
