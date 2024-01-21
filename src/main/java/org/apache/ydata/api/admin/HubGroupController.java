package org.apache.ydata.api.admin;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.ydata.adapter.ZyAdapter;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.model.hub.HubGroup;
import org.apache.ydata.model.hub.HubGroupBot;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubGroupBotService;
import org.apache.ydata.service.hub.HubGroupService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.utils.ResponseCode;
import org.apache.ydata.utils.Tools;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.apache.ydata.vo.PageVo;
import org.apache.ydata.vo.user.HubGroupVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/admin/business/hubgroup")
public class HubGroupController {

    @Resource
    private HttpServletRequest request;

    @Value("${config.prod}")
    private Boolean prod;

    @Resource
    private IdUtil idUtil;

    @Resource
    private HubInfoService hubInfoService;

    @Resource
    private HubGroupService service;

    @Resource
    private HubGroupBotService hubGroupBotService;

    @PostMapping(value = "/create")
    public Object create(HubGroupVo vo) throws UnsupportedEncodingException {
        log.info("创建三方平台群组信息：{}", JSONObject.toJSONString(vo));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");
        if(ObjectUtils.isEmpty(vo.getHubName())) {
            return Tools.createResponse(ResponseCode.ERROR, "创建失败，未选择三方", null);
        }
        HubGroup hubGroup = new HubGroup();
        hubGroup.setId(idUtil.nextId());
        hubGroup.setTelGroupName(vo.getTelGroupName());
        hubGroup.setTelChatId(vo.getTelChatId());
        HubInfo hubInfo = hubInfoService.selectByName(vo.getHubName());
        hubGroup.setHubId(hubInfo != null ? hubInfo.getId() : 0);
        hubGroup.setMemo(vo.getMemo());
        hubGroup.setCreated(System.currentTimeMillis());
        hubGroup.setState(vo.getState());
        long row = service.getMapper().insert(hubGroup);
        if(row > 0) {
            //组的机器人信息
            String groupBotInfo = vo.getGroupBotInfo();
            if (!ObjectUtils.isEmpty(groupBotInfo)) {
                String[] botInfos = groupBotInfo.trim().replaceAll("\n", ",").split(",");
                for (String botInfo : botInfos) {
                    String[] botInfoArray = botInfo.split("-");
                    HubGroupBot bot = new HubGroupBot();
                    bot.setId(idUtil.nextId());
                    bot.setGroupId(hubGroup.getId());
                    bot.setTelChatId(vo.getTelChatId());
                    bot.setTelBotId(botInfoArray.length > 0 ? Long.parseLong(botInfoArray[0]) : -1);
                    bot.setTelBotUsername(botInfoArray.length > 1 ? botInfoArray[1] : "");
                    bot.setMemo(botInfoArray.length > 2 ? botInfoArray[2] : "");
                    bot.setState(vo.getState());
                    bot.setCreated(System.currentTimeMillis());
                    hubGroupBotService.getMapper().insert(bot);
                }
            }
        }

        return Tools.createResponse(ResponseCode.SUCCESS, "创建成功", null);
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
        HubGroup groupInfo = (HubGroup) service.getMapper().selectByPrimaryKey(primaryKey);
        HubInfo hubInfo = hubInfoService.selectByPrimaryKey(groupInfo.getHubId());
        groupInfo.setHubName(hubInfo != null ? hubInfo.getName() : "");
        //查询组的机器人信息
        List<HubGroupBot> botList = hubGroupBotService.selectByGroupId(groupInfo.getId());
        if(!ObjectUtils.isEmpty(botList)) {
            StringBuffer botInfo = new StringBuffer();
            botList.forEach(hubGroupBot -> {
                botInfo.append(hubGroupBot.getTelBotId()).append("-").append(hubGroupBot.getTelBotUsername()).append("\n");
            });
            groupInfo.setGroupBotInfo(botInfo.toString());
        }

        return Tools.createResponse(ResponseCode.SUCCESS,null, groupInfo);
    }

    @PostMapping(value = "/update")
    public Object update(HubGroupVo vo) {
        log.info("更新三方平台群组信息：{}", JSONObject.toJSONString(vo));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        if(ObjectUtils.isEmpty(vo.getHubName())) {
            return Tools.createResponse(ResponseCode.ERROR, "更新失败，未选择三方", null);
        }
        if(vo.getId() == null) {
            return Tools.createResponse(ResponseCode.ERROR, null, null);
        }
        HubGroup hubGroup = (HubGroup) service.getMapper().selectByPrimaryKey(vo.getId());
        if(hubGroup == null) {
            return Tools.createResponse(ResponseCode.ERROR, "数据不存在", null);
        }
        //校验群组ID重复添加情况
//        HubGroup groupInfo1 = service.selectByChatId(vo.getTelChatId());
//        if(groupInfo1 != null && groupInfo1.getId().compareTo(vo.getId()) != 0) {
//            return Tools.createResponse(ResponseCode.ERROR, "更新失败，名称重复！", null);
//        }
        hubGroup.setTelChatId(vo.getTelChatId());
        hubGroup.setTelGroupName(vo.getTelGroupName());
        HubInfo hubInfo = hubInfoService.selectByName(vo.getHubName());
        hubGroup.setHubId(hubInfo != null ? hubInfo.getId() : 0);
        hubGroup.setMemo(vo.getMemo());
        hubGroup.setState(vo.getState());
        hubGroup.setUpdated(System.currentTimeMillis());
        service.getMapper().updateByPrimaryKeySelective(hubGroup);
        //删除组的机器人信息
        hubGroupBotService.deleteAllByGroupId(vo.getId());
        //组的机器人信息
        String groupBotInfo = vo.getGroupBotInfo();
        if (!ObjectUtils.isEmpty(groupBotInfo)) {
            String[] botInfos = groupBotInfo.trim().replaceAll("\n", ",").split(",");
            for (String botInfo : botInfos) {
                String[] botInfoArray = botInfo.split("-");
                HubGroupBot bot = new HubGroupBot();
                bot.setId(idUtil.nextId());
                bot.setGroupId(vo.getId());
                bot.setTelChatId(vo.getTelChatId());
                bot.setTelBotId(botInfoArray.length > 0 ? Long.parseLong(botInfoArray[0]) : -1);
                bot.setTelBotUsername(botInfoArray.length > 1 ? botInfoArray[1] : "");
                bot.setMemo(botInfoArray.length > 2 ? botInfoArray[2] : "");
                bot.setState(vo.getState());
                bot.setCreated(System.currentTimeMillis());
                hubGroupBotService.getMapper().insert(bot);
            }
        }
        return Tools.createResponse(ResponseCode.SUCCESS, null, hubGroup);
    }

    @DeleteMapping(value = "/deleteOne/{primaryKey}")
    public Object deleteOne(@PathVariable Long primaryKey) {
        log.info("删除三方平台群组 【{}】", JSONObject.toJSONString(primaryKey));
        Subject subject = SecurityUtils.getSubject();
        Admin optAdmin = subject.getPrincipal() instanceof Admin ? (Admin) subject.getPrincipal() : null;
        log.info("操作人：{}", optAdmin != null ? optAdmin.getUsername() : "null");

        HubGroup hubGroup = (HubGroup) service.getMapper().selectByPrimaryKey(primaryKey);
        if(hubGroup != null) {
            service.getMapper().delete(hubGroup);
            //删除组的机器人信息
            hubGroupBotService.deleteAllByGroupId(hubGroup.getId());
        }
        return Tools.createResponse(ResponseCode.SUCCESS,"删除成功", null);
    }
}
