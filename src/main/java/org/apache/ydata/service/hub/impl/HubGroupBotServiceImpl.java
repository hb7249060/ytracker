package org.apache.ydata.service.hub.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.mapper.hub.HubGroupBotMapper;
import org.apache.ydata.model.hub.HubGroup;
import org.apache.ydata.model.hub.HubGroupBot;
import org.apache.ydata.service.hub.HubGroupBotService;
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
public class HubGroupBotServiceImpl implements HubGroupBotService {

    @Resource
    private HubGroupBotMapper mapper;

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
        List<HubGroupBot> data = mapper.selectByCondition(condition);

        PageResult pageResult = PageUtils.getPageResult(pageRequest, new PageInfo(data));
        return pageResult;
    }

    @Override
    public HubGroupBot selectByChatIdAndBotId(long chatId, Long botId) {
        HubGroupBot bot = new HubGroupBot();
        bot.setTelChatId(chatId);
        bot.setTelBotId(botId);
        return mapper.selectOne(bot);
    }

    @Override
    public void deleteAllByGroupId(Long groupId) {
        mapper.deleteAllByGroupId(groupId);
    }

    @Override
    public List<HubGroupBot> selectByGroupId(Long groupId) {
        Condition condition = new Condition(HubGroupBot.class);
        condition.createCriteria().andEqualTo("groupId", groupId);
        condition.orderBy("id").asc();
        return mapper.selectByCondition(condition);
    }

    @Override
    public List<HubGroupBot> selectByBotId(Long botId) {
        Condition condition = new Condition(HubGroupBot.class);
        condition.createCriteria().andEqualTo("telBotId", botId);
        return mapper.selectByCondition(condition);
    }
}
