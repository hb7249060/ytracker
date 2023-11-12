package org.apache.ydata.api.admin;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.model.common.SystemSettings;
import org.apache.ydata.service.common.SystemSettingsService;
import org.apache.ydata.utils.GoogleAuthenticationTool;
import org.apache.ydata.utils.ResponseCode;
import org.apache.ydata.utils.Tools;
import org.apache.ydata.vo.SystemSettingsKeys;
import org.apache.ydata.vo.admin.SystemSettingsVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(value = "/admin/system/settings/")
public class SystemSettingsController {

//    @Resource
//    private RedisTemplate redisTemplate;

    @Resource
    private SystemSettingsService systemSettingsService;

    @Value("${config.prod}")
    private Boolean prod;

    @GetMapping(value = "/get")
    public Object getSystemSettings() {
        List<SystemSettings> settings = systemSettingsService.getMapper().selectAll();
        Map dataMap = new HashMap<>();
        if(!ObjectUtils.isEmpty(settings)) {
            settings.forEach(item -> {
                dataMap.put(item.getName(), item.getValue());
            });
        }
        return Tools.createResponse(ResponseCode.SUCCESS, "", dataMap);
    }

    @PostMapping(value = "/save")
    public Object saveSystemSettings(SystemSettingsVo vo) {
        log.info("保存系统设置：{}", JSONObject.toJSONString(vo));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        if(prod) {
            //校验当前登录用户的google验证器
            String rightGoogleCode = GoogleAuthenticationTool.getTOTPCode(optAdmin.getTwoFactorCode());
            if (ObjectUtils.isEmpty(vo.getGoogleCode())) {
                return Tools.createResponse(ResponseCode.ERROR, "请输入谷歌验证码", null);
            }
            if (!vo.getGoogleCode().equals(rightGoogleCode)) {
                log.info("创建失败，谷歌验证码不正确或已超时，超管用户名：{}", optAdmin.getUsername());
                return Tools.createResponse(ResponseCode.ERROR, "谷歌验证码不正确或已超时", null);
            }
        }

        if(vo == null) {
            return Tools.createResponse(ResponseCode.ERROR, "未提交数据", vo);
        }
//        if(ObjectUtils.isEmpty(vo.getDEFAULT_WEB_API_PUB_KEY())
//            || ObjectUtils.isEmpty(vo.getDEFAULT_WEB_API_PRI_KEY())) {
//            return Tools.createResponse(ResponseCode.ERROR, "数据提交出错，请刷新后重试", vo);
//        }
        if(vo != null) {
            Map<String, String> dataMap = vo.toDataMap();
            Iterator<String> iterator = dataMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = dataMap.get(key);
                if(key.equals(SystemSettingsKeys.NOTIFY_URL) && !ObjectUtils.isEmpty(value)) {
                    if(!value.startsWith("http")) {
                        return Tools.createResponse(ResponseCode.ERROR, "收银台地址配置错误", vo);
                    }
                }
            }
            //保存最新配置
            systemSettingsService.deleteAll();
            dataMap.forEach((k,v) -> {
                systemSettingsService.getMapper().insert(new SystemSettings(k, v));
            });
            //存入redis
//            redisTemplate.opsForValue().set(RedisKeys.SYSTEM_PROP, dataMap);
        }
        return Tools.createResponse(ResponseCode.SUCCESS, "保存成功", vo);
    }


    @GetMapping(value = "/generatePriKey")
    public Object generatePriKey() {
        return Tools.createResponse(ResponseCode.SUCCESS, "", Tools.encryptMD5(UUID.randomUUID().toString()));
    }

    @GetMapping(value = "/generatePubKey")
    public Object generatePubKey() {
        return Tools.createResponse(ResponseCode.SUCCESS, "", Tools.getSalt(10));
    }
}
