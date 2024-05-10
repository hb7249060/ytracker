package org.apache.ydata.service.hub.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.mapper.hub.HubInfoMapper;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.utils.Tools;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class HubInfoServiceImpl implements HubInfoService {

    @Resource
    private HubInfoMapper mapper;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        Condition condition = new Condition(HubInfo.class);
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
        List<HubInfo> data = mapper.selectByCondition(condition);

//        long userNumber = countByPageCondition(pageRequest);
//        double totalPoints = sumByPageCondition(pageRequest);
//        String statDesc = "<span style='font-weight: bold;'>结果统计：</span>码商数量 <span style='color:#19a97b;font-weight: bold;'>%s</span>，总积分 <span style='color:#19a97b;font-weight: bold;'>%s</span>";
//        statDesc = String.format(statDesc, userNumber, totalPoints);

        PageResult pageResult = PageUtils.getPageResult(pageRequest, new PageInfo(data));
//        pageResult.setStatDesc(statDesc);
        return pageResult;
    }

    @Override
    public HubInfo selectByName(String username) {
        Condition condition = new Condition(HubInfo.class);
        condition.createCriteria().andEqualTo("name", username);
        List<HubInfo> list = mapper.selectByCondition(condition);
        return Tools.isEmptyList(list) ? null : list.get(0);
    }

    @Override
    public void updateByPrimaryKeySelective(HubInfo userInfo) {
        mapper.updateByPrimaryKeySelective(userInfo);
    }

    @Override
    public List<HubInfo> selectAll(Integer state) {
        Condition condition = new Condition(HubInfo.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andNotEqualTo("state", -1);
        if(state != null) {
            criteria.andEqualTo("state", state);
        }
        return mapper.selectByCondition(condition);
    }

    @Override
    public List<HubInfo> selectByIds(List<Long> hubIdList) {
        return ObjectUtils.isEmpty(hubIdList) ? null : mapper.selectByIds(hubIdList);
    }

    @Override
    public PageResult stat(PageRequest pageRequest) throws ParseException {
        Condition condition = new Condition(HubInfo.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!ObjectUtils.isEmpty(pageRequest.getUserName())) {
            HubInfo hubInfo = selectByName(pageRequest.getUserName().trim());
            if(hubInfo != null) {
                criteria.andEqualTo("id", hubInfo.getId());
            }
        }
        condition.setOrderByClause("id ASC");
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<HubInfo> data = mapper.selectByCondition(condition);
        //根据日期计算日终（默认今天）
        Long startTime = Tools.parseDateString(Tools.formatDateString(new Date(), "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
        Long endTime = Tools.parseDateString( Tools.formatDateString(new Date(), "yyyy-MM-dd") + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime();
        if(!ObjectUtils.isEmpty(pageRequest.getStatDate())) {
            startTime = Tools.parseDateString(pageRequest.getStatDate() + " 00:00:00", "yyyy-MM-dd HH:mm:ss").getTime();
            endTime = Tools.parseDateString( pageRequest.getStatDate() + " 23:59:59", "yyyy-MM-dd HH:mm:ss").getTime();
        }
        if(!ObjectUtils.isEmpty(data)) {
            for (int i = 0; i < data.size(); i++) {
                HubInfo hubInfo = data.get(i);
//                hubInfo.setStatDate(Tools.timeDateString(startTime, "yyyy-MM-dd"));
//                //按渠道统计，先分开统计，再汇总
//                //  取Hub配置的支付通道
//                List<HubPayChannel> channelList = hubPayChannelService.selectByHubId(hubInfo.getId());
//                //将统计数据注入
//                hubInfo.setHubPayChannelList(channelList);
            }
        }
        return PageUtils.getPageResult(pageRequest, new PageInfo(data));
    }

    @Override
    public int insert(HubInfo hubInfo) {
        int rs = mapper.insert(hubInfo);
        return rs;
    }

    @Override
    public HubInfo selectByPrimaryKey(Long id) {
        HubInfo userInfo = mapper.selectByPrimaryKey(id);
        return userInfo;
    }

    @Override
    public int updateByPrimaryKey(HubInfo hubInfo) {
        return mapper.updateByPrimaryKey(hubInfo);
    }

}
