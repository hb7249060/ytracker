package org.apache.ydata.service.common.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ydata.mapper.common.AdminMapper;
import org.apache.ydata.mapper.BaseMapper;
import org.apache.ydata.model.common.Admin;
import org.apache.ydata.service.common.AdminService;
import org.apache.ydata.utils.PageUtils;
import org.apache.ydata.utils.Tools;
import org.apache.ydata.vo.PageRequest;
import org.apache.ydata.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper mapper;

    @Override
    public BaseMapper getMapper() {
        return mapper;
    }

    @Override
    public PageResult getListByPageRequest(PageRequest pageRequest) {
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
        Condition condition = new Condition(Admin.class);
        condition.createCriteria().andIsNotNull("created");
        condition.orderBy("created").desc();
        List<Admin> data = mapper.selectByCondition(condition);
        if(!ObjectUtils.isEmpty(data)) {
            data.forEach(admin -> {
                admin.setPassword(admin.getPassword().substring(0, 4) + "****" + admin.getPassword().substring(admin.getPassword().length() - 4));
                if(!ObjectUtils.isEmpty(admin.getTwoFactorCode())) {
                    admin.setTwoFactorCode(admin.getTwoFactorCode().substring(0, 4) + "****" + admin.getTwoFactorCode().substring(admin.getTwoFactorCode().length() - 4));
                }
            });
        }
        return PageUtils.getPageResult(pageRequest, new PageInfo(data));
    }

    @Override
    public Admin findByUsername(String username) {
        Condition condition = new Condition(Admin.class);
        condition.createCriteria().andEqualTo("username", username);
        List<Admin> list = mapper.selectByCondition(condition);
        return Tools.isEmptyList(list) ? null : list.get(0);
    }
}
