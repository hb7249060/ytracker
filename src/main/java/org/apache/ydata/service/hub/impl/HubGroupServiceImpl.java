package org.apache.ydata.service.hub.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.mapper.hub.HubGroupMapper;
import org.apache.ydata.model.hub.HubGroup;
import org.apache.ydata.model.hub.HubGroupBot;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubGroupBotService;
import org.apache.ydata.service.hub.HubGroupService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class HubGroupServiceImpl implements HubGroupService {

    @Resource
    private HubGroupMapper mapper;

    @Resource
    private HubInfoService hubInfoService;

    @Resource
    private HubGroupBotService hubGroupBotService;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        Condition condition = new Condition(HubGroup.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!ObjectUtils.isEmpty(pageRequest.getUserName())) {
            criteria.andEqualTo("name", pageRequest.getUserName());
        }
        if(pageRequest.getState() != null) {
            criteria.andEqualTo("state", pageRequest.getState());
        }
        criteria.andNotEqualTo("state", -1);
        condition.setOrderByClause("id ASC");
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<HubGroup> data = mapper.selectByCondition(condition);
        if(!ObjectUtils.isEmpty(data)) {
            for(HubGroup hubGroup : data) {
                HubInfo hubInfo = hubInfoService.selectByPrimaryKey(hubGroup.getHubId());
                hubGroup.setHubName(hubInfo != null ? hubInfo.getName() : "");
                //查询组的机器人信息
                List<HubGroupBot> botList = hubGroupBotService.selectByGroupId(hubGroup.getId());
                if(!ObjectUtils.isEmpty(botList)) {
                    StringBuffer botInfo = new StringBuffer();
                    botList.forEach(hubGroupBot -> {
                        botInfo.append(hubGroupBot.getTelBotId()).append("-").append(hubGroupBot.getTelBotUsername()).append("\n");
                    });
                    hubGroup.setGroupBotInfo(botInfo.toString());
                }
            }
        }

        PageResult pageResult = PageUtils.getPageResult(pageRequest, new PageInfo(data));
        return pageResult;
    }

}
