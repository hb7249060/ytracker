package org.apache.ydata.api.admin;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.model.hub.BotAccount;
import org.apache.ydata.model.hub.HubGroup;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.BotAccountService;
import org.apache.ydata.service.hub.HubGroupService;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.utils.ResponseCode;
import org.apache.ydata.utils.Tools;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.PageVo;
import org.apache.ydata.vo.user.HubGroupVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping(value = "/admin/business/botaccount")
public class BotAccountController {

    @Resource
    private HttpServletRequest request;

    @Value("${config.prod}")
    private Boolean prod;

    @Resource
    private IdUtil idUtil;


    @Resource
    private BotAccountService service;

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
        BotAccount botAccount = (BotAccount) service.getMapper().selectByPrimaryKey(primaryKey);
        return Tools.createResponse(ResponseCode.SUCCESS,null, botAccount);
    }

    @DeleteMapping(value = "/deleteOne/{primaryKey}")
    public Object deleteOne(@PathVariable Long primaryKey) {
        log.info("删除bot账号信息 【{}】", JSONObject.toJSONString(primaryKey));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        BotAccount botAccount = (BotAccount) service.getMapper().selectByPrimaryKey(primaryKey);
        if(botAccount != null) service.getMapper().delete(botAccount);
        return Tools.createResponse(ResponseCode.SUCCESS,"删除成功", null);
    }
}
