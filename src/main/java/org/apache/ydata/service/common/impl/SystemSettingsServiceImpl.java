package org.apache.ydata.service.common.impl;

import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.mapper.common.SystemSettingsMapper;
import org.apache.ydata.model.common.SystemSettings;
import org.apache.ydata.service.common.SystemSettingsService;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Condition;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    @Autowired
    private SystemSettingsMapper mapper;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        return null;
    }

    @Override
    public void deleteAll() {
        Condition condition = new Condition(SystemSettings.class);
        mapper.deleteByCondition(condition);
    }
}
