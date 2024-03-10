package org.apache.ydata.service.hub.impl;

import com.beust.jcommander.internal.Maps;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.mapper.hub.HubRechargeRecordMapper;
import org.apache.ydata.model.hub.HubData;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.model.hub.HubRechargeRecord;
import org.apache.ydata.service.hub.HubInfoService;
import org.apache.ydata.service.hub.HubRechargeRecordService;
import org.apache.ydata.utils.IdUtil;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HubRechargeRecordServiceImpl implements HubRechargeRecordService {

    @Resource
    private IdUtil idUtil;

    @Resource
    private HubInfoService hubInfoService;

    @Resource
    private HubRechargeRecordMapper mapper;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        Condition condition = new Condition(HubRechargeRecord.class);
        Example.Criteria criteria = condition.createCriteria();
        if(!ObjectUtils.isEmpty(pageRequest.getUserName())) {
            HubInfo hubInfo = hubInfoService.selectByName(pageRequest.getUserName().trim());
            if(hubInfo != null) {
                criteria.andEqualTo("hubId", hubInfo.getId());
            }
        }
        condition.setOrderByClause("id DESC");
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        List<HubRechargeRecord> data = mapper.selectByCondition(condition);

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
    public int insert(HubRechargeRecord rechargeRecord) {
        return mapper.insert(rechargeRecord);
    }
}
