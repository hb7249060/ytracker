package org.apache.ydata.service.hub.impl;

import com.alibaba.fastjson2.JSONObject;
import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.mapper.hub.HubDataMapper;
import org.apache.ydata.model.hub.HubData;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.hub.HubDataService;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HubDataServiceImpl implements HubDataService {

    @Resource
    private IdUtil idUtil;

    @Resource
    private HubInfoService hubInfoService;

    @Resource
    private HubDataMapper mapper;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        Condition condition = new Condition(HubData.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!ObjectUtils.isEmpty(pageRequest.getUserName())) {
            HubInfo hubInfo = hubInfoService.selectByName(pageRequest.getUserName().trim());
            if(hubInfo != null) {
                criteria.andEqualTo("hubId", hubInfo.getId());
            }
        }
        if(!ObjectUtils.isEmpty(pageRequest.getStatDate())) {
            criteria.andEqualTo("statDate", pageRequest.getStatDate().replaceAll("-", ""));
        }
        condition.setOrderByClause("stat_date DESC, benfit DESC");
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<HubData> data = mapper.selectByCondition(condition);

        List<HubInfo> allHubs = hubInfoService.selectAll();
        Map<Long, HubInfo> hubInfoDataMap = !ObjectUtils.isEmpty(allHubs) ? allHubs.stream().collect(Collectors.toMap(HubInfo::getId, HubInfo -> HubInfo)) : Maps.newHashMap();
        if(!ObjectUtils.isEmpty(data)) {
            data.forEach(item -> {
                item.setHubName(hubInfoDataMap.get(item.getHubId()).getName());
            });
        }

        PageResult pageResult = PageUtils.getPageResult(pageRequest, new PageInfo(data));
        return pageResult;
    }

    @Override
    public HubData insertOrUpdate(HubInfo hubInfo, JSONObject jsonDataObject) {
        if(hubInfo == null || jsonDataObject == null || !jsonDataObject.containsKey("statDate")) {
            return null;
        }
        String statDate = jsonDataObject.getString("statDate").replaceAll("-", "");
        HubData hubData = selectByHubIdAndStatDate(hubInfo.getId(), statDate);

        if(hubData == null) {
            //insert
            hubData = new HubData();
            hubData.setId(idUtil.nextId());
            hubData.setHubId(hubInfo.getId());
            hubData.setStatDate(statDate);
            hubData.setCreated(System.currentTimeMillis());
            hubData.setUpdated(System.currentTimeMillis());
            hubData.setOrderTotalCount(jsonDataObject.getLong("orderTotalCount"));
            hubData.setOrderTotalPayCount(jsonDataObject.getLong("orderTotalPayCount"));
            hubData.setOrderTotalFee(jsonDataObject.getDouble("orderTotalFee"));
            hubData.setOrderPayFee(jsonDataObject.getDouble("orderPayFee"));
            hubData.setOrderSuccessRate(jsonDataObject.getDouble("orderSuccessRate"));
            if(hubInfo.getRate() != null) {
                hubData.setBenfit(new BigDecimal(hubData.getOrderPayFee().doubleValue())
                        .multiply(new BigDecimal(hubInfo.getRate().doubleValue()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }

            hubData.setSysBalance(jsonDataObject.getDouble("sysBalance"));
            mapper.insert(hubData);
        } else {
            //update
            hubData.setUpdated(System.currentTimeMillis());
            hubData.setOrderTotalCount(jsonDataObject.getLong("orderTotalCount"));
            hubData.setOrderTotalPayCount(jsonDataObject.getLong("orderTotalPayCount"));
            hubData.setOrderTotalFee(jsonDataObject.getDouble("orderTotalFee"));
            hubData.setOrderPayFee(jsonDataObject.getDouble("orderPayFee"));
            hubData.setOrderSuccessRate(jsonDataObject.getDouble("orderSuccessRate"));
            if(hubInfo.getRate() != null) {
                hubData.setBenfit(new BigDecimal(hubData.getOrderPayFee().doubleValue())
                        .multiply(new BigDecimal(hubInfo.getRate().doubleValue()))
                        .setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
            }
            if(jsonDataObject.containsKey("sysBalance")) {
                hubData.setSysBalance(jsonDataObject.getDouble("sysBalance"));
            }
            mapper.updateByPrimaryKeySelective(hubData);
        }
        return hubData;
    }

    @Override
    public List<HubData> selectByDateAndHubIds(String statDate, List<Long> hubIds) {
        Condition condition = new Condition(HubData.class);
        Example.Criteria criteria = condition.createCriteria();
        criteria.andEqualTo("statDate", statDate.replaceAll("-", ""));
        criteria.andIn("hubId", hubIds);
        return mapper.selectByCondition(condition);
    }

    private HubData selectByHubIdAndStatDate(Long hubId, String statDate) {
        HubData hubData = new HubData();
        hubData.setHubId(hubId);
        hubData.setStatDate(statDate.replaceAll("-", ""));
        return mapper.selectOne(hubData);
    }
}
