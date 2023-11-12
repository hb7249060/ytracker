package org.apache.ydata.api.admin;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.model.hub.HubData;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubDataService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.*;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.PageVo;
import org.apache.ydata.vo.user.HubInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;

@Slf4j
@RestController
@RequestMapping(value = "/admin/business/hubinfo")
public class HubInfoController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private HubInfoService service;

    @Value("${config.prod}")
    private Boolean prod;

    @Resource
    private IdUtil idUtil;

    @Resource
    private ZyAdapter zyAdapter;

    @Resource
    private HubDataService hubDataService;

    @GetMapping(value = "/getall")
    public Object all() {
        return Tools.createResponse(ResponseCode.SUCCESS, "", service.selectAll());
    }

    @PostMapping(value = "/create")
    public Object create(HubInfoVo vo) throws UnsupportedEncodingException {
        log.info("创建三方平台：{}", JSONObject.toJSONString(vo));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        if(prod) {
            //校验当前登录用户的google验证器
//            String rightGoogleCode = GoogleAuthenticationTool.getTOTPCode(optAdmin.getTwoFactorCode());
//            if (ObjectUtils.isEmpty(vo.getGoogleCode())) {
//                return Tools.createResponse(ResponseCode.ERROR, "请输入谷歌验证码", null);
//            }
//            if (!vo.getGoogleCode().equals(rightGoogleCode)) {
//                log.info("创建失败，谷歌验证码不正确或已超时，超管用户名：{}", optAdmin.getUsername());
//                return Tools.createResponse(ResponseCode.ERROR, "谷歌验证码不正确或已超时", null);
//            }
        }

        HubInfo hubInfo = new HubInfo();
        hubInfo.setId(idUtil.nextId());
        hubInfo.setName(vo.getName());
        hubInfo.setRate(vo.getRate());
        hubInfo.setApiUrl(vo.getApiUrl());
        hubInfo.setMemo(vo.getMemo());
        hubInfo.setCreated(System.currentTimeMillis());
        hubInfo.setState(vo.getState());
        service.insert(hubInfo);
        return Tools.createResponse(ResponseCode.SUCCESS, "创建成功", null);
    }

    @RequestMapping(value = "/getList")
    public Object getList(@RequestBody PageVo pageVo) {
        PageResult pageResult = service.getListByPageRequest(PageRequest.get(pageVo));
        return PageUtils.getPager(pageResult);
    }

    @RequestMapping(value = "/stat")
    public Object stat(@RequestBody PageVo pageVo) {
        PageResult pageResult = null;
        try {
            pageResult = service.stat(PageRequest.get(pageVo));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return PageUtils.getPager(pageResult);
    }

    @RequestMapping(value = "/getListByPage/{pageNum}/{pageSize}")
    public Object getListByPage(int pageNum, int pageSize) {
        PageResult pageResult = service.getListByPageRequest(new PageRequest(pageNum, pageSize));
        return pageResult;
    }

    @GetMapping(value = "/getOne/{primaryKey}")
    public Object getOne(@PathVariable Object primaryKey) {
        HubInfo userinfo = (HubInfo) service.getMapper().selectByPrimaryKey(primaryKey);
        return Tools.createResponse(ResponseCode.SUCCESS,null, userinfo);
    }

    @PostMapping(value = "/update")
    public Object update(HubInfoVo vo) {
        log.info("更新三方平台信息：{}", JSONObject.toJSONString(vo));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        if(prod) {
            //校验当前登录用户的google验证器
//            String rightGoogleCode = GoogleAuthenticationTool.getTOTPCode(optAdmin.getTwoFactorCode());
//            if (ObjectUtils.isEmpty(vo.getGoogleCode())) {
//                return Tools.createResponse(ResponseCode.ERROR, "请输入谷歌验证码", null);
//            }
//            if (!vo.getGoogleCode().equals(rightGoogleCode)) {
//                log.info("创建失败，谷歌验证码不正确或已超时，超管用户名：{}", optAdmin.getUsername());
//                return Tools.createResponse(ResponseCode.ERROR, "谷歌验证码不正确或已超时", null);
//            }
        }

        if(vo.getId() == null) {
            return Tools.createResponse(ResponseCode.ERROR, null, null);
        }
        HubInfo hubInfo = (HubInfo) service.getMapper().selectByPrimaryKey(vo.getId());
        if(hubInfo == null) {
            return Tools.createResponse(ResponseCode.ERROR, "数据不存在", null);
        }
        //校验重名
        HubInfo hubInfo1 = service.selectByName(vo.getName().trim());
        if(hubInfo1 != null && hubInfo1.getId().compareTo(hubInfo.getId()) != 0) {
            return Tools.createResponse(ResponseCode.ERROR, "创建失败，名称重复！", null);
        }
        hubInfo.setName(vo.getName());
        hubInfo.setRate(vo.getRate());
        hubInfo.setApiUrl(vo.getApiUrl());
        hubInfo.setMemo(vo.getMemo());
        hubInfo.setState(vo.getState());
        hubInfo.setUpdated(System.currentTimeMillis());
        service.getMapper().updateByPrimaryKeySelective(hubInfo);
        return Tools.createResponse(ResponseCode.SUCCESS, null, hubInfo);
    }

    @DeleteMapping(value = "/deleteOne/{primaryKey}")
    public Object deleteOne(@PathVariable Long primaryKey) {
        log.info("删除三方平台 【{}】", JSONObject.toJSONString(primaryKey));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        HubInfo hubInfo = service.selectByPrimaryKey(primaryKey);
        hubInfo.setState(-1);
        service.updateByPrimaryKey(hubInfo);
        return Tools.createResponse(ResponseCode.SUCCESS,"删除成功", null);
    }

    @GetMapping(value = "/viewStat/{primaryKey}")
    public Object viewStat(@PathVariable Object primaryKey) {
        HubInfo hubInfo = (HubInfo) service.getMapper().selectByPrimaryKey(primaryKey);
        try {
            JSONObject jsonObject = zyAdapter.stat(hubInfo, Tools.timeDateString(System.currentTimeMillis(), "yyyy-MM-dd"));
            //解析并写入库
            HubData hubData = hubDataService.insertOrUpdate(hubInfo, jsonObject);
            hubInfo.setHubData(hubData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Tools.createResponse(ResponseCode.SUCCESS,null, hubInfo);
    }
}
