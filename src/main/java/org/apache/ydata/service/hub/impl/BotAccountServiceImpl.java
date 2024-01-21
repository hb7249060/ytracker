package org.apache.ydata.service.hub.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.mapper.hub.BotAccountMapper;
import org.apache.ydata.model.hub.BotAccount;
import org.apache.ydata.service.hub.BotAccountService;
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
public class BotAccountServiceImpl implements BotAccountService {

    @Resource
    private BotAccountMapper mapper;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        Condition condition = new Condition(BotAccount.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!ObjectUtils.isEmpty(pageRequest.getUserName())) {
            criteria.andEqualTo("username", pageRequest.getUserName());
        }
        condition.setOrderByClause("id ASC");
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<BotAccount> data = mapper.selectByCondition(condition);

        PageResult pageResult = PageUtils.getPageResult(pageRequest, new PageInfo(data));
        return pageResult;
    }

    @Override
    public BotAccount selectByBotId(Long userId) {
        return mapper.selectByPrimaryKey(userId);
    }

    @Override
    public synchronized BotAccount save(Long userId, String username, String nickname) {
        try {
            BotAccount account = new BotAccount();
            account.setId(userId);
            account.setUsername(username);
            account.setNickname(nickname);
            account.setCreated(System.currentTimeMillis());
            mapper.insert(account);
            return account;
        } catch (Exception e) {
//            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            return null;
        }
    }
}
