package org.apache.ydata.service.common.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ydata.mapper.common.AdminLogMapper;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.common.AdminLog;
import org.apache.ydata.model.hub.HubInfo;
import org.apache.ydata.service.common.AdminLogService;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdminLogServiceImpl implements AdminLogService {

    @Autowired
    private AdminLogMapper mapper;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        Example example = new Example(HubInfo.class);
        example.setOrderByClause("create_time DESC");
        List<AdminLog> data = mapper.selectByExample(example);
        return PageUtils.getPageResult(pageRequest, new PageInfo(data));
    }

}
