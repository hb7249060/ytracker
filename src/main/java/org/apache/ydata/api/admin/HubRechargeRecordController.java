package org.apache.ydata.api.admin;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.model.hub.HubRechargeRecord;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.service.hub.HubRechargeRecordService;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.utils.ResponseCode;
import org.apache.ydata.utils.Tools;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.PageVo;
import org.apache.ydata.vo.user.HubInfoVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping(value = "/admin/business/hubrechargerecord")
public class HubRechargeRecordController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private HubInfoService hubInfoService;

    @Value("${config.prod}")
    private Boolean prod;

    @Resource
    private IdUtil idUtil;

    @Resource
    private ZyAdapter zyAdapter;

    @Resource
    private HubRechargeRecordService service;

    @PostMapping(value = "/create")
    public Object create(HubRechargeRecord vo) throws UnsupportedEncodingException {
        log.info("创建加款记录平台：{}", JSONObject.toJSONString(vo));
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
        if(vo.getAmount() == null) {
            return Tools.createResponse(ResponseCode.ERROR, "加款失败，金额输入有误", null);
        }

        HubInfo hubInfo = hubInfoService.selectByName(vo.getHubName());
        if(hubInfo == null) {
            return Tools.createResponse(ResponseCode.ERROR, "加款失败，三方信息不存在", null);
        }
        JSONObject jsonObject = zyAdapter.addSysFee(hubInfo, vo);
        if(jsonObject.containsKey("code") && jsonObject.getIntValue("code") == 200) {
            HubRechargeRecord rechargeRecord = new HubRechargeRecord();
            rechargeRecord.setId(idUtil.nextId());
            rechargeRecord.setHubId(hubInfo != null ? hubInfo.getId() : 0);
            rechargeRecord.setAmount(vo.getAmount());
            rechargeRecord.setMemo(vo.getMemo());
            rechargeRecord.setCreated(System.currentTimeMillis());
            service.insert(rechargeRecord);
            return Tools.createResponse(ResponseCode.SUCCESS, "加款成功", null);
        } else {
            return jsonObject;
        }
    }

    @RequestMapping(value = "/getList")
    public Object getList(@RequestBody PageVo pageVo) {
        PageResult pageResult = service.getListByPageRequest(PageRequest.get(pageVo));
        return PageUtils.getPager(pageResult);
    }

    @RequestMapping(value = "/getListByPage/{pageNum}/{pageSize}")
    public Object getListByPage(int pageNum, int pageSize) {
        PageResult pageResult = service.getListByPageRequest(new PageRequest(pageNum, pageSize));
        return pageResult;
    }

    @GetMapping(value = "/getOne/{primaryKey}")
    public Object getOne(@PathVariable Object primaryKey) {
        HubRechargeRecord rechargeRecord = (HubRechargeRecord) service.getMapper().selectByPrimaryKey(primaryKey);
        return Tools.createResponse(ResponseCode.SUCCESS,null, rechargeRecord);
    }

    @PostMapping(value = "/update")
    public Object update(HubInfoVo vo) {
        return Tools.createResponse(ResponseCode.ERROR, "加款记录不可更新", null);
    }

    @DeleteMapping(value = "/deleteOne/{primaryKey}")
    public Object deleteOne(@PathVariable Long primaryKey) {
        return Tools.createResponse(ResponseCode.ERROR, "加款记录不可删除", null);
    }

}
